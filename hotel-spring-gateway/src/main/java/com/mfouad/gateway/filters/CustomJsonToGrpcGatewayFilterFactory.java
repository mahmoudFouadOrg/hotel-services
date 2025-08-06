package com.mfouad.gateway.filters;

import java.io.IOException;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CustomJsonToGrpcGatewayFilterFactory extends AbstractGatewayFilterFactory<CustomJsonToGrpcGatewayFilterFactory.Config> {

    private final ResourceLoader resourceLoader;

    public CustomJsonToGrpcGatewayFilterFactory(ResourceLoader resourceLoader) {
        super(Config.class);
        this.resourceLoader = resourceLoader;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Validate config values
            if (config.getProtoDescriptor() == null) {
                return Mono.error(new IllegalArgumentException("protoDescriptor must not be null"));
            }
            // Load resource reactively
            try {
				resourceLoader.getResource(config.getProtoDescriptor()).getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return Mono.fromCallable(() -> resourceLoader.getResource(config.getProtoDescriptor()))
                .flatMap(resource -> {
                    if (!resource.exists()) {
                        return Mono.error(new IllegalArgumentException("Resource not found: " + config.getProtoDescriptor()));
                    }
                    return Mono.fromCallable(resource::getInputStream)
                        .doOnNext(inputStream -> System.out.println("Loaded descriptor: " + config.getProtoDescriptor()))
                        .then(chain.filter(exchange));
                })
                .onErrorResume(e -> Mono.error(new IllegalArgumentException("Failed to load descriptor: " + config.getProtoDescriptor(), e)));
        };
    }

    public static class Config {
        private String protoDescriptor;
        private String protoFile;
        private String service;
        private String method;

        // Getters and setters
        public String getProtoDescriptor() { return protoDescriptor; }
        public void setProtoDescriptor(String protoDescriptor) { this.protoDescriptor = protoDescriptor; }
        public String getProtoFile() { return protoFile; }
        public void setProtoFile(String protoFile) { this.protoFile = protoFile; }
        public String getService() { return service; }
        public void setService(String service) { this.service = service; }
        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
    }
}