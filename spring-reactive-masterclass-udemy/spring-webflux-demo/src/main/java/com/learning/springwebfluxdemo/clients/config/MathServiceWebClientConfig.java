package com.learning.springwebfluxdemo.clients.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientRequest.Builder;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class MathServiceWebClientConfig {

	private static final String MATHS_SERVICE_BASE_URL = "http://localhost:9000/maths";

	@Bean
	public WebClient mathWebClient() {
		return WebClient.builder()
				// cross cutting, at webclient level itslef setting auth, will be common for all
				// the component using this bean
				// will be called only once at the time of server startuop
//				.defaultHeaders(headers -> {
//					log.info("mathWebClient: setting basic auth");
//					headers.setBasicAuth("kbhatt23", "kanishklikesprogramming");
//				})

				// for each request it could be different
				// add interceptor that gets called at runtime
				.filter(this::addBearerToken).baseUrl(MATHS_SERVICE_BASE_URL).build();
	}

	// acting as an interceptor
	// at the moment of actual request this gets called, we can add bearer token for
	// all request as per need dynamically
	// will be called for each request where the webclient is used
	private Mono<ClientResponse> addBearerToken(ClientRequest request, ExchangeFunction next) {
		log.info("addBearerToken: Setting bearer token");
		
		ClientRequest requestModified = request.attribute("auth-type")
			   .map(authType -> "basic".equals(authType) ? basicAuth(request) : bearerToken(request) )
			   .orElse(request)
			   ;
		
//		ClientRequest requestModified = ClientRequest.from(request)
//				.headers(header -> header.setBearerAuth("jai shree ram")).build();
		return next.exchange(requestModified);
	}
	
	private static ClientRequest basicAuth(ClientRequest clientRequest) {
		
		return ClientRequest.from(clientRequest)
					.headers(headers -> headers.setBasicAuth("kbhatt23", "kanishklikesprogramming"))
					.build();
	}
	
	private static ClientRequest bearerToken(ClientRequest clientRequest) {
		
		return ClientRequest.from(clientRequest)
					.headers(headers -> headers.setBearerAuth("jai shree ram"))
					.build();
	}
}
