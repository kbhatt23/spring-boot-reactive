package com.learning.users_service.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.learning.users_service.dtos.UserTransactionDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(value = "user_transactions")
public class UserTransactionEntity {
	@Id
	private Long id;
	
	private double amount;
	
	//bidirectional
	@Column("user_id")
	private Long userID;
	
	@Column("transaction_time")
	private LocalDateTime localDateTime = LocalDateTime.now();
	
	@Column("transaction_type")
	private TransactionType transactionType;
	
	public UserTransactionEntity(UserTransactionDTO userTransactionDTO) {
		if(userTransactionDTO == null)
			throw new IllegalArgumentException("userTransactionDTO can not be null");
		
		this.id=userTransactionDTO.getId();
		this.amount=userTransactionDTO.getAmount();
		this.localDateTime = userTransactionDTO.getLocalDateTime();
		this.userID = userTransactionDTO.getUserID();
		this.transactionType=userTransactionDTO.getTransactionType();
	}
}
