package com.learning.products_service.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.learning.products_service.documents.ProductDocument;

import reactor.core.publisher.Flux;

@Repository
public interface ProductsRepository extends ReactiveMongoRepository<ProductDocument, String> {

	
	public Flux<ProductDocument> findByPriceBetween(double minPrice, double maxPrice);
}
