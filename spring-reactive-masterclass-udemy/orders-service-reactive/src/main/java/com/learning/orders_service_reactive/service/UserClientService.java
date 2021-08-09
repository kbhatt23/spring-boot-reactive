package com.learning.orders_service_reactive.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.learning.orders_service_reactive.dtos.UserDTO;
import com.learning.orders_service_reactive.dtos.UserTransactionDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Service
public class UserClientService {

	@Autowired
	private WebClient usersWebClient;
	
	public Mono<UserTransactionDTO> debitAmount(Mono<UserTransactionDTO> userTransactionDTOMono){
		return usersWebClient.post()
					 .uri("/debit")
					 .contentType(MediaType.APPLICATION_JSON)
				     .body(userTransactionDTOMono, UserTransactionDTO.class)
				     .retrieve()
				     .bodyToMono(UserTransactionDTO.class)
				     .retryWhen(Retry.fixedDelay(4, Duration.ofSeconds(1)))
				     .log("debitAmount: ")
				     ;
	}
	
public Mono<UserDTO> findUser(long userID){
		
		return usersWebClient.get()
					 .uri("/{userID}", userID)
				     .accept(MediaType.APPLICATION_JSON)
				     .retrieve()
				     .bodyToMono(UserDTO.class)
				     .retryWhen(Retry.fixedDelay(4, Duration.ofSeconds(1)))
				     .log("findUser: ")
				     ;
	}

public Flux<UserDTO> findAllUsers(){
	return usersWebClient.get()
					    .accept(MediaType.TEXT_EVENT_STREAM)
			    		.retrieve()
			    		.bodyToFlux(UserDTO.class);
}
	
}
