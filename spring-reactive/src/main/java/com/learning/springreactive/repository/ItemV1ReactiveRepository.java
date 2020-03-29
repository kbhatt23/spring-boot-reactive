package com.learning.springreactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.learning.springreactive.document.ItemV1;

@Repository
public interface ItemV1ReactiveRepository extends ReactiveMongoRepository<ItemV1, String>{

}
