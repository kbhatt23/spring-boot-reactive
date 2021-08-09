package com.learning.users_service.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.learning.users_service.dtos.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(value = "users")
public class UserEntity {
	@Id
	private Long id;

	private String name;

	private double balance;

//	private List<UserTransactionEntity> userTransactions;
//	
//	public UserEntity addTransaction(UserTransactionEntity userTransactionEntity) {
//		if(userTransactions == null)
//			userTransactions = new ArrayList<>();
//		
//		userTransactions.add(userTransactionEntity);
//		userTransactionEntity.setUserID(this.id);
//		
//		return this;
//	}
	
	public UserEntity(UserDTO userDTO) {
		if(userDTO == null)
			throw new IllegalArgumentException("userDTO can not be null");
		
		this.id=userDTO.getId();
		this.name=userDTO.getName();
		this.balance=userDTO.getBalance();
//		if(userDTO.getUserTransactions() != null)
//			this.userTransactions=userDTO.getUserTransactions().stream().map(UserTransactionEntity :: new).collect(Collectors.toList());
	}
	
}
