package com.mfouad.gateway.filters;

import java.net.URI;

import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.mfouad.gateway.HotelJson;
import com.mfouad.proto.HotelSearch;

import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CustomJsonToGrpc extends AbstractGatewayFilterFactory<CustomJsonToGrpc.Config> {

	@Override
	public GatewayFilter apply(Config config) {
		// TODO Auto-generated method stub
		return (exchange, chain) -> {
			GRPCResponseDecorator modifiedResponse = new GRPCResponseDecorator(exchange);
			
			 return modifiedResponse
                     .writeWith(exchange.getRequest().getBody())
                     .then(chain.filter(exchange.mutate()
                             .response(modifiedResponse).build()));
		};
	}

	@Data
	@NoArgsConstructor
	public static class Config {

	}

	static class GRPCResponseDecorator extends ServerHttpResponseDecorator {

		private ServerWebExchange exchange;

		public GRPCResponseDecorator(ServerWebExchange exchange) {
			super(exchange.getResponse());
			this.exchange = exchange;
		}

		public GRPCResponseDecorator(ServerHttpResponse delegate) {
			super(delegate);
		}
		
		@Override
		public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
			exchange.getResponse().getHeaders().set("Content-Type", "application/json");
			 URI requestURI = exchange.getRequest().getURI();
//			 return getDelegate().writeWith(deserializeJSONRequest()
//					 .map(jsonRequest ->{return 
//						 
//					 } )
//					 );
			 
			 return null;
			
		}
		
		private Flux<HotelJson> deserializeJSONRequest() {
            return exchange.getRequest()
                    .getBody()
                    .mapNotNull(dataBufferBody -> {
                        ResolvableType targetType = ResolvableType.forType(HotelJson.class);
                        return new Jackson2JsonDecoder()
                                .decode(dataBufferBody, targetType, null, null);
                    })
                    .cast(HotelJson.class);
        }
		
		
	}
}
