package com.mfouad;

import io.vertx.mutiny.core.http.HttpClient;
import io.vertx.mutiny.core.Vertx;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class QuarkusGateway {
	
	@Inject
	Vertx vertx;
	
	private HttpClient httpClient;
	
	@PostConstruct
	 void init() {
		this.httpClient = vertx.createHttpClient();
	}

}
