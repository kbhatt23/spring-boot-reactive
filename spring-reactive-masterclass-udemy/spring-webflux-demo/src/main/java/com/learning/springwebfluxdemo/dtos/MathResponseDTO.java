package com.learning.springwebfluxdemo.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MathResponseDTO {

	private double result;
	
	private LocalDateTime localDateTime;

	public MathResponseDTO(double result) {
		this.result = result;
		this.localDateTime = LocalDateTime.now();
	}
	
	
}
