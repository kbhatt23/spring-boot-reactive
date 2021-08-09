package com.learning.orders_service_reactive.dtos;

import java.time.LocalDateTime;

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
	
}
