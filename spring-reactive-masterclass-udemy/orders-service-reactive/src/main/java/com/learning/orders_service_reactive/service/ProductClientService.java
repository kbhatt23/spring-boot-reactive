package com.learning.orders_service_reactive.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.learning.orders_service_reactive.dtos.ProductDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Service
public class ProductClientService {

	@Autowired
	private WebClient productsWebClient;
	
	public Mono<ProductDTO> findProduct(String productID){
		
		return productsWebClient.get()
					 .uri("/{productID}", productID)
				     .accept(MediaType.APPLICATION_JSON)
				     .retrieve()
				     .bodyToMono(ProductDTO.class)
				     .retryWhen(Retry.fixedDelay(4, Duration.ofSeconds(1)))
				     .log("findProduct: ")
				     ;
	}
	
	public Flux<ProductDTO> findAllProducts(){
		return productsWebClient.get()
							.accept(MediaType.TEXT_EVENT_STREAM)
				    		.retrieve()
				    		.bodyToFlux(ProductDTO.class);
	}
	
}
