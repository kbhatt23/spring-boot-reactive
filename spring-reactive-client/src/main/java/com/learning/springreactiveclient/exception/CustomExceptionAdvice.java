package com.learning.springreactiveclient.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionAdvice {

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<CustomExceptionResponse> handlecustomException(RuntimeException exc){
		CustomExceptionResponse res = new CustomExceptionResponse(new Date(),
				exc.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString());
		return new ResponseEntity<CustomExceptionResponse>(res, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
