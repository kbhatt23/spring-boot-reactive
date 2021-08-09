package com.learning.orders_service_reactive.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

	private Long id;
	
	private String name;
	
	private double balance;
	
	//private List<UserTransactionDTO> userTransactions;
	
}
