package com.learning.springwebfluxdemo.handlers;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Service
public class BasicMonoHandler{

	public Mono<ServerResponse> basicMono(ServerRequest serverRequest){
		
		Mono<ServerResponse> res=	 ServerResponse.ok().body(Mono.just("jai shree ram says basic mono"), String.class);
		return res;
	}

}
