package com.learning.springwebfluxdemo.handlers;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.learning.springwebfluxdemo.dtos.MathResponseDTO;

import reactor.core.publisher.Mono;

@Service
public class CalculatorService {

	
	public Mono<ServerResponse> addition(ServerRequest serverRequest){
		int num1 = Integer.parseInt(serverRequest.pathVariable("num1"));
		
		int num2 = Integer.parseInt(serverRequest.pathVariable("num2"));
		
		return ServerResponse.ok().body(Mono.create(sink -> {
			sink.success(new MathResponseDTO(num1 + num2));
		}), MathResponseDTO.class);
	}
	
	public Mono<ServerResponse> substraction(ServerRequest serverRequest){
		int num1 = Integer.parseInt(serverRequest.pathVariable("num1"));
		
		int num2 = Integer.parseInt(serverRequest.pathVariable("num2"));
		
		return ServerResponse.ok().body(Mono.create(sink -> {
			sink.success(new MathResponseDTO(num1 - num2));
		}), MathResponseDTO.class);
	}
	
	public Mono<ServerResponse> division(ServerRequest serverRequest){
		int num1 = Integer.parseInt(serverRequest.pathVariable("num1"));
		
		int num2 = Integer.parseInt(serverRequest.pathVariable("num2"));
		
		return ServerResponse.ok().body(Mono.create(sink -> {
			sink.success(new MathResponseDTO(num1 / num2));
		}), MathResponseDTO.class);
	}
	
	public Mono<ServerResponse> multiplication(ServerRequest serverRequest){
		int num1 = Integer.parseInt(serverRequest.pathVariable("num1"));
		
		int num2 = Integer.parseInt(serverRequest.pathVariable("num2"));
		
		return ServerResponse.ok().body(Mono.create(sink -> {
			sink.success(new MathResponseDTO(num1 * num2));
		}), MathResponseDTO.class);
	}
}
