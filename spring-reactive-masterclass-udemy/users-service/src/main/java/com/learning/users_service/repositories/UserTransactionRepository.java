package com.learning.users_service.repositories;

import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

import com.learning.users_service.entities.UserTransactionEntity;

import reactor.core.publisher.Flux;

@Repository
public interface UserTransactionRepository extends ReactiveSortingRepository<UserTransactionEntity, Long>{

	public Flux<UserTransactionEntity> findByUserID(long userID);
}
