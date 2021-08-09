package com.learning.orders_service_reactive.error;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CustomErrorMessage {

	private String message;
	
	private LocalDateTime localDateTime;

	public CustomErrorMessage(String message, LocalDateTime localDateTime) {
		this.message = message;
		this.localDateTime = localDateTime;
	}
	
	
}
