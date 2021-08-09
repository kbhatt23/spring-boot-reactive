package com.learning.springwebfluxdemo.handlers;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.learning.springwebfluxdemo.dtos.MathResponseDTO;
import com.learning.springwebfluxdemo.dtos.MultiplyRequest;
import com.learning.springwebfluxdemo.exception.CustomErrorMessage;
import com.learning.springwebfluxdemo.exception.InvalidInputException;
import com.learning.springwebfluxdemo.services.MathsWebFluxService;

import reactor.core.publisher.Mono;

@Service
public class MathsServiceHandler {
	
	@Autowired
	private MathsWebFluxService mathsWebFluxService;

	public Mono<ServerResponse> squareRootHandler(ServerRequest serverRequest){
		
		int number = Integer.parseInt(serverRequest.pathVariable("number"));
		
		return ServerResponse.ok().body(mathsWebFluxService.squareRootLazy(number), MathResponseDTO.class);
	}
	
	public Mono<ServerResponse> numberTableHandler(ServerRequest serverRequest){
		
		int number = Integer.parseInt(serverRequest.pathVariable("number"));
		
		return ServerResponse
				.ok()
				.contentType(MediaType.TEXT_EVENT_STREAM)
				.body(mathsWebFluxService.numberTableLazy(number), MathResponseDTO.class)
				;
	}
	
	public Mono<ServerResponse> multpliyNumbersHandler(ServerRequest serverRequest){
		Mono<MultiplyRequest> requestMono = serverRequest.bodyToMono(MultiplyRequest.class);
		
		//System.out.println("multpliyNumbersHandler: headers found "+serverRequest.headers().toString());
		
		return ServerResponse
				//.ok()
				.created(URI.create("/hello"))
				.contentType(MediaType.TEXT_EVENT_STREAM)
				.body(mathsWebFluxService.multipleLazily(requestMono), MathResponseDTO.class)
				;
	}
	
	public Mono<ServerResponse> errorTypeHandler(ServerRequest  serverRequest){
		Function<Integer, Mono<ServerResponse>> transform= input -> Mono.error(() -> new InvalidInputException(0, null));
	return Mono.just(Integer.parseInt(serverRequest.pathVariable("input")))
			   .filter(input -> input < 10 || input > 20)
			   .flatMap(input -> ServerResponse.badRequest().body(Mono.just(new CustomErrorMessage("invalid input passed", LocalDateTime.now())), CustomErrorMessage.class))
			   //.flatMap(transform)
			   .switchIfEmpty(ServerResponse.ok().bodyValue("jai shree ram"))
				;
	}
	
	
	
	
}
