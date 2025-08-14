package com.mfouad.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HotelGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelGatewayApplication.class, args);
	}
	
	@RequestMapping("/fallback")
    public ResponseEntity<String> fallback() {
        return new ResponseEntity<>("Service is temporarily unavailable. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
    }
	
        			
	
}
