package com.mfouad.gateway.filters;

import java.nio.charset.StandardCharsets;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.util.JsonFormat;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class JsonToProtobufRequestFilter extends AbstractGatewayFilterFactory<JsonToProtobufRequestFilter.Config> {

    public JsonToProtobufRequestFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            
            // Only process if Content-Type is JSON
            if (!isJsonRequest(request)) {
                log.debug("Skipping filter - not a JSON request. Content-Type: {}", 
                    request.getHeaders().getContentType());
                return chain.filter(exchange);
            }

            // Check if body exists
            if (request.getHeaders().getContentLength() == 0) {
                log.warn("Request has Content-Length: 0, skipping conversion");
                return chain.filter(exchange);
            }

            log.debug("Converting JSON request to Protobuf for: {} with Content-Length: {}", 
                request.getPath(), request.getHeaders().getContentLength());

            return request.getBody()
                .collectList()
                .flatMap(dataBuffers -> {
                    if (dataBuffers.isEmpty()) {
                        log.warn("No data buffers received from request body");
                        return chain.filter(exchange);
                    }
                    
                    return DataBufferUtils.join(Flux.fromIterable(dataBuffers));
                })
                .cast(DataBuffer.class)
                .flatMap(dataBuffer -> {
                    try {
                        // Read JSON payload
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        DataBufferUtils.release(dataBuffer);
                        
                        String jsonPayload = new String(bytes, StandardCharsets.UTF_8);
                        log.debug("Original JSON: {}", jsonPayload);

                        // Get protobuf descriptor
                        FileDescriptor fileDescriptor = com.mfouad.proto.HotelSearch.getDescriptor();
                        Descriptors.Descriptor messageDescriptor = getMessageDescriptor(fileDescriptor, config.getRequestType());
                        
                        // Convert JSON to Protobuf
                        DynamicMessage.Builder builder = DynamicMessage.newBuilder(messageDescriptor);
                        JsonFormat.parser()
                            .ignoringUnknownFields()
                            .merge(jsonPayload, builder);
                        
                        DynamicMessage message = builder.build();
                        byte[] protobufBytes = message.toByteArray();
                        
                        log.debug("Converted to protobuf, size: {} bytes", protobufBytes.length);

                        // Create new request with protobuf body and gRPC headers
                        ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(request) {
                            @Override
                            public Flux<DataBuffer> getBody() {
                            	 log.info("Creating new request body with protobuf data");
                            	 
                                return Flux.just(exchange.getResponse()
                                        .bufferFactory()
                                        .wrap(protobufBytes)
                                        );
                            }

                            @Override
                            public HttpHeaders getHeaders() {
                                HttpHeaders headers = HttpHeaders.writableHttpHeaders(super.getHeaders());
                                headers.setContentType(MediaType.valueOf("application/grpc+proto"));
                                headers.set("grpc-encoding", "identity");
                                headers.set("grpc-accept-encoding", "gzip");
                                headers.set("grpc-timeout", "30S");
                                headers.set("te", "trailers");
                                headers.setContentLength(protobufBytes.length);
                                headers.set("accept", "application/grpc+proto");
                                headers.set("Content-Type", "application/grpc+proto");
                                return headers;
                            }
                        };

                        return chain.filter(exchange.mutate().request(mutatedRequest).build());

                    } catch (Exception e) {
                        log.error("Error converting JSON to Protobuf", e);
                        return Mono.error(new RuntimeException("Failed to convert JSON to Protobuf: " + e.getMessage(), e));
                    }
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Empty request body - this might be a GET request or body was already consumed");
                    log.warn("Request method: {}, Content-Type: {}, Content-Length: {}", 
                        request.getMethod(), 
                        request.getHeaders().getContentType(),
                        request.getHeaders().getContentLength());
                    return chain.filter(exchange);
                }));
        };
    }
    
    private boolean isJsonRequest(ServerHttpRequest request) {
        MediaType contentType = request.getHeaders().getContentType();
        return contentType != null && 
               (MediaType.APPLICATION_JSON.isCompatibleWith(contentType) ||
                MediaType.APPLICATION_JSON_UTF8.isCompatibleWith(contentType));
    }
    
    private Descriptors.Descriptor getMessageDescriptor(FileDescriptor fileDescriptor, String messageType) {
        // Try to find the message type in the file descriptor
        for (Descriptors.Descriptor descriptor : fileDescriptor.getMessageTypes()) {
            if (descriptor.getFullName().equals(messageType) || 
                descriptor.getName().equals(messageType)) {
                return descriptor;
            }
        }
        
        // If not found in root, check services for request/response types
        for (Descriptors.ServiceDescriptor service : fileDescriptor.getServices()) {
            for (Descriptors.MethodDescriptor method : service.getMethods()) {
                if (method.getInputType().getFullName().equals(messageType) ||
                    method.getInputType().getName().equals(messageType)) {
                    return method.getInputType();
                }
            }
        }
        
        throw new IllegalArgumentException("Message type not found: " + messageType);
    }

    @Data
    public static class Config {
        private String requestType = "HotelSearchRequest"; // Default message type
        private String protoDescriptor; // Optional: for future use with dynamic loading
    }
}