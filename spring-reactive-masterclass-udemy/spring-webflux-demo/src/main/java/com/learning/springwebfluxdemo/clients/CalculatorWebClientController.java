package com.learning.springwebfluxdemo.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.learning.springwebfluxdemo.clients.config.CalculatorServiceWebClientConfig;
import com.learning.springwebfluxdemo.dtos.MathResponseDTO;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/calculator-client")
public class CalculatorWebClientController {

	@Autowired
	private WebClient calculatorWebClient;
	
	@GetMapping("/add/{num1}/{num2}")
	public Mono<MathResponseDTO> add(@PathVariable int num1 , @PathVariable int num2){
		
		return calculatorWebClient.get()
		    .uri("/{num1}/{num2}", num1,num2)
		    .attribute("operator", CalculatorServiceWebClientConfig.ADDITION)
		    .retrieve()
		    .bodyToMono(MathResponseDTO.class)
		    .log("CalculatorWebClientController.add: ")
		    ;
		    
	}
	
	@GetMapping("/multiply/{num1}/{num2}")
	public Mono<MathResponseDTO> multiply(@PathVariable int num1 , @PathVariable int num2){
		
		return calculatorWebClient.get()
		    .uri("/{num1}/{num2}", num1,num2)
		    .attribute("operator", CalculatorServiceWebClientConfig.MULTIPLICATION)
		    .retrieve()
		    .bodyToMono(MathResponseDTO.class)
		    .log("CalculatorWebClientController.add: ")
		    ;
		    
	}
	
	@GetMapping("/subsctract/{num1}/{num2}")
	public Mono<MathResponseDTO> subsctract(@PathVariable int num1 , @PathVariable int num2){
		
		return calculatorWebClient.get()
		    .uri("/{num1}/{num2}", num1,num2)
		    .attribute("operator", CalculatorServiceWebClientConfig.SUBSTRACTION)
		    .retrieve()
		    .bodyToMono(MathResponseDTO.class)
		    .log("CalculatorWebClientController.add: ")
		    ;
		    
	}
	
	@GetMapping("/division/{num1}/{num2}")
	public Mono<MathResponseDTO> division(@PathVariable int num1 , @PathVariable int num2){
		
		return calculatorWebClient.get()
		    .uri("/{num1}/{num2}", num1,num2)
		    .attribute("operator", CalculatorServiceWebClientConfig.DIVISION)
		    .retrieve()
		    .bodyToMono(MathResponseDTO.class)
		    .log("CalculatorWebClientController.add: ")
		    ;
		    
	}
}
