package com.learning.springwebfluxdemo.routers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.learning.springwebfluxdemo.handlers.BasicMonoHandler;

@Configuration
public class BasicMonoRouter {

	@Autowired
	private BasicMonoHandler basicMonoHandler;
	
	@Bean
	public RouterFunction<ServerResponse> routes(){
		return RouterFunctions.route(RequestPredicates.GET("/functional/mono-basic").and(RequestPredicates.accept(MediaType.APPLICATION_JSON))
				, basicMonoHandler :: basicMono)
				;
	}
}
