package com.learning.springreactive.routers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;


import com.learning.springreactive.handlers.BasicHandlerCompoenent;

@Configuration
public class BasicRouterConfig {

	@Bean
	public RouterFunction<ServerResponse> basicRoute(BasicHandlerCompoenent basicHandler){
		//consumer - > application/json
		//produces -> applicaiton/straem json
		return RouterFunctions.route(GET("/functional/ints").and(accept(MediaType.ALL)),
				basicHandler::fluxStream)
				.andRoute(GET("/functional/int").and(accept(MediaType.ALL)), basicHandler::monoStream)
				;
	}
}
