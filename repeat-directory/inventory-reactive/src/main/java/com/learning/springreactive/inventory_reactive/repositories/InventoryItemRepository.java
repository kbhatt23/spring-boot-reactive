package com.learning.springreactive.inventory_reactive.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.learning.springreactive.inventory_reactive.documents.InventoryItem;

import reactor.core.publisher.Flux;

public interface InventoryItemRepository extends ReactiveMongoRepository<InventoryItem, String>{

	Flux<InventoryItem> findByDescriptionContains(String name);
	
}
