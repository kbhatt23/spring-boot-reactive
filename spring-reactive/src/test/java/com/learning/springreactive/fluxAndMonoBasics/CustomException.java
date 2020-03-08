package com.learning.springreactive.fluxAndMonoBasics;

public class CustomException extends RuntimeException {

	private String message;
	
	public CustomException(String message) {
		this.message = message;
	}
}
