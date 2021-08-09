package com.learning.springwebdemo.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MathResponseDTO {

	private double result;
	
	private LocalDateTime localDateTime;

	public MathResponseDTO(double result) {
		this.result = result;
		this.localDateTime = LocalDateTime.now();
	}
	
	
}
