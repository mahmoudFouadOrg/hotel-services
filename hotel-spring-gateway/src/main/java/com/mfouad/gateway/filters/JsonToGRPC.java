package com.mfouad.gateway.filters;

import org.springframework.cloud.gateway.config.GrpcSslConfigurer;
import org.springframework.cloud.gateway.filter.factory.JsonToGrpcGatewayFilterFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component("JsonToGrpc")
public class JsonToGRPC extends JsonToGrpcGatewayFilterFactory {

	public JsonToGRPC(GrpcSslConfigurer grpcSslConfigurer, ResourceLoader resourceLoader) {
		super(grpcSslConfigurer, resourceLoader);
	}

}
