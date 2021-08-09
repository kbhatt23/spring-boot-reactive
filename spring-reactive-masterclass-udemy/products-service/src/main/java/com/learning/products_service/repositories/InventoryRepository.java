package com.learning.products_service.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;

import com.learning.products_service.documents.InventoryDocument;

import reactor.core.publisher.Flux;

public interface InventoryRepository extends ReactiveMongoRepository<InventoryDocument, Long>{
	
	@Tailable
	Flux<InventoryDocument> findItemsBy();

}
