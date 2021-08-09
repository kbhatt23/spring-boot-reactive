package com.learning.springwebfluxdemo.routers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.learning.springwebfluxdemo.handlers.MathsServiceHandler;

@Configuration
public class MathServiceRouter {
	
	@Autowired
	private MathsServiceHandler mathsServiceHandler;

	@Bean
	public RouterFunction<ServerResponse> routeMathsServices(){
		
		return RouterFunctions.route(RequestPredicates.GET("/functional/maths/squareRoot/{number}"),
				mathsServiceHandler :: squareRootHandler
				)
				.andRoute(RequestPredicates.GET("/functional/maths/numberTable/{number}")
						
						, mathsServiceHandler :: numberTableHandler)
				.andRoute(RequestPredicates.POST("/functional/maths/multiply")
						.and(RequestPredicates.contentType(MediaType.APPLICATION_JSON))
						, mathsServiceHandler :: multpliyNumbersHandler)
				.andRoute(RequestPredicates.GET("/functional/maths/error/{input}")
						, mathsServiceHandler :: errorTypeHandler)
				;
	}
}
