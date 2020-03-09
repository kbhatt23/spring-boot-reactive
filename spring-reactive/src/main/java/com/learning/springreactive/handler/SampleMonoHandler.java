package com.learning.springreactive.handler;

import java.time.Duration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Configuration
public class SampleMonoHandler {

	public Mono<ServerResponse> mono(ServerRequest serverRequest){
		return ServerResponse.ok()
							 .contentType(MediaType.APPLICATION_JSON)
							 .body(Mono.fromSupplier(() -> 1) , Integer.class);
	}
	
	public Mono<ServerResponse> monoStream(ServerRequest serverRequest){
		return ServerResponse.ok()
							 .contentType(MediaType.APPLICATION_STREAM_JSON)
							 .body(Mono.fromSupplier(() -> 1).delayElement(Duration.ofSeconds(2)) , Integer.class);
	}
}
