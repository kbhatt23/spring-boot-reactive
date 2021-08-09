package com.learning.springwebdemo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.springwebdemo.dtos.MathResponseDTO;
import com.learning.springwebdemo.services.MathsWebService;

@RestController
@RequestMapping("/maths")
public class MathWebController {
	@Autowired
	private MathsWebService mathsService; 

	@GetMapping("/squareRoot/{number}")
	public MathResponseDTO squareRoot(@PathVariable("number") int number) {
		
		return mathsService.squareRoot(number);
	}
	
	@GetMapping("/numberTable/{number}")
	public List<MathResponseDTO> numberTable(@PathVariable("number") int number) {
		
		return mathsService.numberTable(number);
	}
}
