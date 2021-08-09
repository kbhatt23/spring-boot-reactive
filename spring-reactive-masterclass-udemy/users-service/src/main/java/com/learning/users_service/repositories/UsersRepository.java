package com.learning.users_service.repositories;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

import com.learning.users_service.entities.UserEntity;

import reactor.core.publisher.Mono;

@Repository
public interface UsersRepository extends ReactiveSortingRepository<UserEntity, Long>{

	//manual query for efficeincy
	//sql query and not jpql
	//returns true if balance is greater than or equal to amount
	//returns false if balance is lesser than amount
	@Query("update users set balance = balance - :amount where id = :userID and balance >= :amount")
	@Modifying
	public Mono<Boolean> debitUserBalance(long userID , double amount);
	
	
	//assuming single transaction can only credit 100 bucks at max
	@Query("update users set balance = balance + :amount where id = :userID and :amount <= 1000")
	@Modifying
	public Mono<Boolean> creditUserBalance(long userID , double amount);
}
