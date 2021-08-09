package com.learning.springwebfluxdemo.exception;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@ControllerAdvice
public class CustomExcpetinHandler {

	@ExceptionHandler(InvalidInputException.class)
	public ResponseEntity<CustomErrorMessage> handleError(InvalidInputException exception
			){
		CustomErrorMessage customErrorMessage = new CustomErrorMessage(exception.getMessage(), LocalDateTime.now());
		return ResponseEntity.badRequest().body(customErrorMessage);
		
	}
}
