package com.learning.users_service.dtos;

import java.time.LocalDateTime;

import com.learning.users_service.entities.TransactionType;
import com.learning.users_service.entities.UserTransactionEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//no need to be bidirectional
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTransactionDTO {
	private Long id;
	

	private double amount;
	
	private Long userID;
	
	private TransactionType transactionType;
	//eager so that constructor work is saved
	//during copy time it gets overriden in copy constructor
	private LocalDateTime localDateTime = LocalDateTime.now();
	
	public UserTransactionDTO(UserTransactionEntity userTransactionEntity) {
		if(userTransactionEntity == null)
			throw new IllegalArgumentException("userTransactionEntity can not be null");
		
		this.id= userTransactionEntity.getId();
		this.amount=userTransactionEntity.getAmount();
		this.userID= userTransactionEntity.getUserID();
		this.localDateTime = userTransactionEntity.getLocalDateTime();
		this.transactionType=userTransactionEntity.getTransactionType();
	}
}
