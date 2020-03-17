package com.learning.springreactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;

import com.learning.springreactive.document.CappedItem;

import reactor.core.publisher.Flux;

@Repository
public interface CappedItemReactiveRepository extends ReactiveMongoRepository<CappedItem, String>{
	
	//we need to create tailable curson, whihc keeps on the connection established and 
	//do not rmeove it
	@Tailable
	Flux<CappedItem> findItemsBy();

}
