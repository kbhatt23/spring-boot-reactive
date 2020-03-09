package com.learning.springreactive.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import com.learning.springreactive.handler.SampleMonoHandler;

@Configuration
public class MonoRouter {

	@Bean
	public RouterFunction<ServerResponse> routeMono(SampleMonoHandler handler){
		
		return RouterFunctions.route(GET("/functional/mono")
								.and(accept(MediaType.APPLICATION_JSON))
				, handler::mono)
				.andRoute(GET("/functional/monoStream")
						.and(accept(MediaType.APPLICATION_JSON))
						, 
						handler::monoStream)
				;
	}
}
