package com.mfouad.gateway.filters;

import java.nio.charset.StandardCharsets;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.util.JsonFormat;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component


public class ProtobufToJsonResponseFilter extends AbstractGatewayFilterFactory<ProtobufToJsonResponseFilter.Config> {
	 private final ObjectMapper objectMapper = new ObjectMapper();

	    public ProtobufToJsonResponseFilter() {
	        super(Config.class);
	    }

	    @Override
	    public GatewayFilter apply(Config config) {
	        return (exchange, chain) -> chain.filter(exchange)
	            .then(Mono.fromRunnable(() -> {
	                if (exchange.getResponse().getHeaders().getContentType().includes(MediaType.valueOf("application/protobuf"))) {
	                    exchange.getResponse().getBody().subscribe(dataBuffer -> {
	                        try {
	                            byte[] bytes = new byte[dataBuffer.readableByteCount()];
	                            dataBuffer.read(bytes);
	                            
	                            DynamicMessage message = DynamicMessage.parseFrom(
	                                ProtobufUtil.getDescriptor(config.getResponseType()),
	                                bytes
	                            );
	                            String json = JsonFormat.printer().print(message);
	                            
	                            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
	                            exchange.getResponse().writeWith(Flux.just(
	                                exchange.getResponse().bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8))
	                            ));
	                        } catch (Exception e) {
	                            throw new RuntimeException("Conversion failed", e);
	                        }
	                    });
	                }
	            }));
	    }

	    @Data
	    public static class Config {
	        private String responseType;
	        // getters/setters
	    }

}
