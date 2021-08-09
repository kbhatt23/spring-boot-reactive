package com.learning.users_service.dtos;

import com.learning.users_service.entities.UserEntity;

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
	
	public UserDTO(UserEntity userEntity) {
		if(userEntity == null)
			throw new IllegalArgumentException("userEntity can not be null");
		
		this.id= userEntity.getId();
		this.name=userEntity.getName();
		this.balance = userEntity.getBalance();
//		if(userEntity.getUserTransactions() != null)
//			this.userTransactions = userEntity.getUserTransactions().stream().map(UserTransactionDTO :: new).collect(Collectors.toList());
	}
}
