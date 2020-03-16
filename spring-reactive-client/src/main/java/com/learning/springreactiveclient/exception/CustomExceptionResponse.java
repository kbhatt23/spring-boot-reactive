package com.learning.springreactiveclient.exception;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomExceptionResponse {

	private Date date;
	
	private String errorMessage;
	
	private String status;
}
