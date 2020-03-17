package com.learning.springreactive.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.learning.springreactive.handler.CappedItemHandler;

@Configuration
public class CappedItemRouter {

	@Bean
	RouterFunction<ServerResponse> routeCappedItems(CappedItemHandler handler){
	
		return RouterFunctions.route(RequestPredicates.GET("/v2/cappedItems"),handler::handleCappedItems);
		
	}
}
