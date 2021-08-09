package com.learning.orders_service_reactive.dtos;

import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

import com.learning.orders_service_reactive.entities.OrderEntity;

import reactor.core.publisher.Flux;

@Repository
public interface OrderRespository extends ReactiveSortingRepository<OrderEntity, Long>{
	
	public Flux<OrderEntity> findByUserID(long userID);
}
