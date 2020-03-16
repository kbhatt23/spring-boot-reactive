package com.learning.springreactive.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

import com.learning.springreactive.handler.ItemHandler;

@Configuration
public class ItemRouter {

	@Bean
	RouterFunction<ServerResponse> routeFindAllItem(ItemHandler itemHandler){
		return RouterFunctions.route(
				//setting the urlk here like we do in @getmapping
				GET("/v2/items").and(accept(MediaType.APPLICATION_JSON_UTF8)),
				itemHandler::findAllWithException
				)
				//similar to @getmapping path variable
				.andRoute(GET("/v2/items/{itemId}")
							.and(accept(MediaType.APPLICATION_JSON))
						, itemHandler::findById)
				.andRoute(POST("/v2/items")
						.and(accept(MediaType.APPLICATION_JSON))
						, itemHandler::createItem)
				.andRoute(DELETE("/v2/items/{itemId}")
							.and(accept(MediaType.APPLICATION_JSON))
						, itemHandler::deleteById)
				.andRoute(PUT("/v2/items")
						.and(accept(MediaType.APPLICATION_JSON))
						, itemHandler::updateItem)
				
				;
	}
	
}
