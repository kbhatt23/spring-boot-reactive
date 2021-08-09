package com.learning.orders_service_reactive.dtos;

import com.learning.orders_service_reactive.entities.OrderEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

	private Long id;
	
	private Long userID;
	
	private String productID;
	
	private double price;
	
	private Long transactionId;
	
	public OrderDTO(OrderEntity orderEntity) {
		if(orderEntity == null)
			throw new IllegalArgumentException("orderEntity can not be null");
		
		this.id=orderEntity.getId();
		this.userID=orderEntity.getUserID();
		this.productID=orderEntity.getProductID();
		this.price=orderEntity.getPrice();
		this.transactionId=orderEntity.getTransactionId();
	}

	public OrderDTO(Long userID, String productId) {
		this.userID=userID;
		this.productID=productId;
	}
	
}
