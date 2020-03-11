package com.learning.springreactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.learning.springreactive.document.Item;

import reactor.core.publisher.Flux;
@Repository
public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String>{

	//custom operation
	//finding documesnt by non ID property returns more than one,
	//hence using Flux
	Flux<Item> findByDescriprion(String description);
}
