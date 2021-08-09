package com.learning.orders_service_reactive.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.learning.orders_service_reactive.dtos.OrderDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//R2dbc: can not use @entity , @onetoone,manually set id of foreign key, manually create order table etc
@Table("orders")
public class OrderEntity {

	@Id
	private Long id;

	@Column("user_id")
	private Long userID;

	@Column("product_id")
	private String productID;

	private double price;
	
	@Column("transaction_id")
	private Long transactionId;
	
	public OrderEntity(OrderDTO orderDTO) {
		if(orderDTO == null)
			throw new IllegalArgumentException("orderDTO can not be null");
		
		this.id=orderDTO.getId();
		this.userID=orderDTO.getUserID();
		this.productID=orderDTO.getProductID();
		this.price=orderDTO.getPrice();
		this.transactionId=orderDTO.getTransactionId();
	}
}
