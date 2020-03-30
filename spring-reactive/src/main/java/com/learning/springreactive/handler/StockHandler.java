package com.learning.springreactive.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.learning.springreactive.document.Stock;
import com.learning.springreactive.repository.StockService;

import reactor.core.publisher.Mono;

@Component
public class StockHandler {

	private final StockService stockService;
	public StockHandler(StockService stockService) {
		this.stockService = stockService;
	}
	
	public Mono<ServerResponse> findAllStreamByName(ServerRequest serverRequest){
		
		String name = serverRequest.pathVariable("name");
		return ServerResponse.ok()
					   .contentType(MediaType.APPLICATION_STREAM_JSON)
					   .body(stockService.findAllByStockName(name), Stock.class);
	}
	
public Mono<ServerResponse> findAllStreamByNameLimit(ServerRequest serverRequest){
		
		String name = serverRequest.pathVariable("name");
		
		Integer limit = Integer.valueOf(serverRequest.pathVariable("limit"));
		
		return ServerResponse.ok()
					   .contentType(MediaType.APPLICATION_STREAM_JSON)
					   .body(stockService.findAllByStockName(name)
							   .take(limit)
							   , Stock.class);
	}
}
