package com.mfouad.gateway.filters;

import org.springframework.cloud.gateway.config.GrpcSslConfigurer;
import org.springframework.cloud.gateway.filter.factory.JsonToGrpcGatewayFilterFactory;
import org.springframework.core.io.ResourceLoader;

public class JsonToGrpc extends JsonToGrpcGatewayFilterFactory{

	public JsonToGrpc(GrpcSslConfigurer grpcSslConfigurer, ResourceLoader resourceLoader) {
		super(grpcSslConfigurer, resourceLoader);
		// TODO Auto-generated constructor stub
	}

}
