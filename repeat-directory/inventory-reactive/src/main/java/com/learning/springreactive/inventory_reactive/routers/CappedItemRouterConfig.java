package com.learning.springreactive.inventory_reactive.routers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class CappedItemRouterConfig {

	
	@Autowired
	private CappedItemHandler handler;
	
	@Bean
	public RouterFunction<ServerResponse> configCappedItemRouter(){
		
		return RouterFunctions.route(GET("/v2/stream"), handler::handleCappedItem);
	}
}
