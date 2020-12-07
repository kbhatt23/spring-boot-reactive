package com.learning.springreactive.handlers;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class BasicHandlerCompoenent {

	
	public Mono<ServerResponse> fluxStream(ServerRequest serverRequest){
		return ServerResponse.ok()
					  .contentType(MediaType.APPLICATION_STREAM_JSON)
					  .body(Flux.just(1,2,3,4,5)
							    .delayElements(Duration.ofSeconds(1))
							    .log()
							  , Integer.class);
	}
	
	public Mono<ServerResponse> monoStream(ServerRequest serverRequest){
		return ServerResponse.ok()
							.contentType(MediaType.APPLICATION_STREAM_JSON)
							.body(Mono.just(108).delayElement(Duration.ofSeconds(1)).log(), Integer.class);
	}
}
