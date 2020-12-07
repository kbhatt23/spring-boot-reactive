package com.learning.springreactive.inventory_reactive.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;

import com.learning.springreactive.inventory_reactive.documents.InventoryItem;
import com.learning.springreactive.inventory_reactive.documents.InventoryItemCapped;

import reactor.core.publisher.Flux;

public interface InventoryItemCappedRepository extends ReactiveMongoRepository<InventoryItemCapped, String>{
	//a never tmerinating connection always waitning for newer document in collection
	@Tailable
	Flux<InventoryItemCapped> findItemsBy();
	
}
