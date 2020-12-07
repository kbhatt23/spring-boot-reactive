package com.learning.springreactive.inventory_reactive.routers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class InventoryITemRouterConfig {

	@Bean
	RouterFunction<ServerResponse> configureRoutes(InventoryItemHandler handler){
		//find all route
		return RouterFunctions.route(GET("/v2/inventory"), handler::handleGetAll)
							  .andRoute(GET("/v2/inventory/{inventoryId}"), handler::handlefindByID)
							  .andRoute(POST("/v2/inventory"), handler::handleCreate)
							  .andRoute(PUT("/v2/inventory/{inventoryId}"), handler::handleUpdate)
							  .andRoute(DELETE("/v2/inventory/{inventoryId}"), handler::handleDelete)
				;
	}
}
