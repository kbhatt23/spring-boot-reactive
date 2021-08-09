package com.learning.springwebfluxdemo.exception;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CustomErrorMessage {

	private String message;
	
	private LocalDateTime localDateTime;

	public CustomErrorMessage(String message, LocalDateTime localDateTime) {
		super();
		this.message = message;
		this.localDateTime = localDateTime;
	}
	
	
}
