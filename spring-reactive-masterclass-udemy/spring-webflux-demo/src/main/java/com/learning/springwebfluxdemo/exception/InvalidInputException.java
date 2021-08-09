package com.learning.springwebfluxdemo.exception;

import lombok.Data;

//we will throw this exception
public class InvalidInputException extends Exception{

	private static final long serialVersionUID = 2145677149342044498L;

	
	private int input;
	
	public InvalidInputException(int input,String message){
		super(message);
		this.input=input;
		
	}

	public int getInput() {
		return input;
	}

	public void setInput(int input) {
		this.input = input;
	}
	
	
}
