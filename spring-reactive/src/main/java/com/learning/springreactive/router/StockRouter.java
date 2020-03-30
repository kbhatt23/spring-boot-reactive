package com.learning.springreactive.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.learning.springreactive.handler.StockHandler;

@Configuration
public class StockRouter {

	@Bean
	public RouterFunction<ServerResponse> stockRoutes(StockHandler stockHandler){
		
		return RouterFunctions.route(
				RequestPredicates.GET("/stocks/{name}")
				.and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8))
				, 
				stockHandler::findAllStreamByName
				)
				.andRoute(RequestPredicates.GET("/stocks/{name}/{limit}")
				.and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8))
				, 
				stockHandler::findAllStreamByNameLimit)
				;
	}
}
