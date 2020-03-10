package com.learning.springreactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.learning.springreactive.document.Item;
@Repository
public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String>{

}
