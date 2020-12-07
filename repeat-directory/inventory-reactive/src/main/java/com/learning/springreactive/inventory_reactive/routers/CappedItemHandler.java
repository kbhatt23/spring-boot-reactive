package com.learning.springreactive.inventory_reactive.routers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.learning.springreactive.inventory_reactive.documents.InventoryItemCapped;
import com.learning.springreactive.inventory_reactive.repositories.InventoryItemCappedRepository;

import reactor.core.publisher.Mono;

@Component
public class CappedItemHandler {

	@Autowired
	private InventoryItemCappedRepository repo;
	
	public Mono<ServerResponse> handleCappedItem(ServerRequest request){
		return ServerResponse.ok()
					 .contentType(MediaType.APPLICATION_STREAM_JSON)
					  .body(repo.findItemsBy(), InventoryItemCapped.class);
	}
}
