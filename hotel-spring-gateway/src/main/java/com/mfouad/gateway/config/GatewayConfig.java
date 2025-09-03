package com.mfouad.gateway.config;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.cloud.gateway.config.HttpClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.HttpProtocol;

@Configuration
public class GatewayConfig {

	@Bean
	public HttpClientCustomizer httpClientCustomizer() {
		return httpClient -> httpClient
                // Enable both HTTP/1.1 and HTTP/2 for downstream services
                .protocol(HttpProtocol.HTTP11, HttpProtocol.H2C) // H2C for HTTP/2 over cleartext
                // Connection settings
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                // Timeouts for gRPC calls
                .doOnConnected(conn -> 
                    conn.addHandlerLast(new ReadTimeoutHandler(30, TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(30, TimeUnit.SECONDS))
                )
                // HTTP/2 specific settings for gRPC
                .http2Settings(settings -> settings
                    .maxConcurrentStreams(1000)
                    .initialWindowSize(1048576) // 1MB window
                    .maxFrameSize(16384)
                    .maxHeaderListSize(8192)
                )
                // Connection pool settings
                .responseTimeout(Duration.ofSeconds(30));
	}
}
