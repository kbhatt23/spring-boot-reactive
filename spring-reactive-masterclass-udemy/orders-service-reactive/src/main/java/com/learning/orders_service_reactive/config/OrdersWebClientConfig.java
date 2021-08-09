package com.learning.orders_service_reactive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OrdersWebClientConfig {

	@Bean
	public WebClient productsWebClient() {
		
		return WebClient.builder()
				.baseUrl("http://localhost:5000/products")
				.build();
	}
	
	@Bean
	public WebClient usersWebClient() {
		return WebClient.builder()
				.baseUrl("http://localhost:1100/users")
				.build();
	}
}
