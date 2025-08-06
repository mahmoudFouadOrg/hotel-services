package com.mfouad.gateway.filters;

import java.nio.charset.StandardCharsets;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;

import com.google.protobuf.DynamicMessage;
import com.google.protobuf.util.JsonFormat;

import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class JsonToProtobufRequestFilter extends AbstractGatewayFilterFactory<JsonToProtobufRequestFilter.Config> {


	public JsonToProtobufRequestFilter() {
		super(Config.class);
	}

	@Override
    public GatewayFilter apply(Config config) {
		 return (exchange, chain) -> {
			 
			 return exchange.getRequest().getBody().collectList()
             .flatMap(list ->  {
            	 Flux<DataBuffer> flux = Flux.fromIterable(list);
            	 return DataBufferUtils.join(flux);
             })
             .flatMap(dataBuffer -> {
                 try {
                	  byte[] bytes = new byte[dataBuffer.readableByteCount()];
                      dataBuffer.read(bytes);
                      DataBufferUtils.release(dataBuffer);

                      // 2. Convert JSON to Protobuf
                      DynamicMessage.Builder builder = DynamicMessage.newBuilder(
                          ProtobufUtil.getDescriptor(config.getRequestType())
                      );
                      JsonFormat.parser().merge(new String(bytes, StandardCharsets.UTF_8), builder);
                      
                      // 3. Create new request with Protobuf body
                      byte[] protobufBytes = builder.build().toByteArray();
                      ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                          @Override
                          public Flux<DataBuffer> getBody() {
                              return Flux.just(exchange.getResponse().bufferFactory().wrap(protobufBytes));
                          }

                          @Override
                          public HttpHeaders getHeaders() {
                              HttpHeaders headers = new HttpHeaders();
                              headers.putAll(super.getHeaders());
                              headers.setContentType(MediaType.valueOf("application/protobuf"));
                              headers.setContentLength(protobufBytes.length);
                              return headers;
                          }
                      };

                      // 4. Continue with the mutated request
                      return chain.filter(exchange.mutate().request(mutatedRequest).build());

                      
                 }
                 catch (Exception e) {
                     return Mono.error(e);
                 }
			 
             });

};
	}

	@Data
	public static class Config {
		private String requestType;
	}
}
