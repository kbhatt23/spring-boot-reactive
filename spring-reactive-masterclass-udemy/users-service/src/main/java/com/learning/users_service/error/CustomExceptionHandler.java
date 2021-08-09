package com.learning.users_service.error;
import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<CustomErrorMessage> handleGenericError(Exception exception
			){
		CustomErrorMessage customErrorMessage = new CustomErrorMessage(exception.getMessage(), LocalDateTime.now());
		return ResponseEntity.badRequest().body(customErrorMessage);
		
	}
}