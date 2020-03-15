package com.learning.springreactiveclient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ReactiveWebClientConfig {

	@Value("${itemServiceBaseUrl}")
	private String itemServiceBaseUrl;
	
	@Bean
	public WebClient webClient() {
		return WebClient.create(itemServiceBaseUrl);
	}
}
