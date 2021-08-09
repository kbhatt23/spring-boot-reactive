package com.learning.springwebfluxdemo.controllers;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.springwebfluxdemo.dtos.MathResponseDTO;
import com.learning.springwebfluxdemo.dtos.MultiplyRequest;
import com.learning.springwebfluxdemo.exception.CustomErrorMessage;
import com.learning.springwebfluxdemo.exception.InvalidInputException;
import com.learning.springwebfluxdemo.services.MathsWebFluxService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/maths")
@Slf4j
public class MathWebFluxController {
	@Autowired
	private MathsWebFluxService mathsService; 

	@GetMapping("/squareRoot/{number}")
	public Mono<MathResponseDTO> squareRoot(@PathVariable("number") int number, @RequestHeader Map<String, String> headers) {
		log.info("squareRoot: headers recieved "+headers);
		return mathsService.squareRootLazy(number);
	}
	
	@GetMapping(path = "/numberTable/{number}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	
	public Flux<MathResponseDTO> numberTable(@PathVariable("number") int number) {
		
		//return mathsService.numberTableEager(number);
		return mathsService.numberTableLazy(number)
				//.take(2)
				;
	}
	
	//post call
	//remember request body can only be Mono and a flux in request body can not be used
	@PostMapping(path = "/multiply" )
	public Mono<MathResponseDTO> multipleTwoNumbers(@RequestBody Mono<MultiplyRequest> request){
		return mathsService.multipleLazily(request);
		
	}
	
	//just demonstrating how error occurs
	//webflux handles it  by default like spring boot
	@GetMapping("/error/{input}")
	public Mono<String> handleError(@PathVariable int input){
		
		if(input < 10 || input > 20) {
			return Mono.error(() -> new InvalidInputException(input,"invalid input passed"));
		}else {
			return Mono.just("jai shree ram");
		}
	}
	
	//send custom entity without use of controller advice
	@GetMapping("/error-new/{input}")
	public Mono<ResponseEntity<?>> handleErrorNew(@PathVariable int input){
		//path variable is already reoslved , valueb bis already calculated and hence using just
	return	Mono.just(input)
		   .handle((data,sink) -> {
			   if(data < 10 || data > 20) {
				   //error but not using controller advice'
				   sink.next(
						   ResponseEntity.badRequest().body(new CustomErrorMessage("invalid input passeed", LocalDateTime.now()))
				   );
			   }else {
				   sink.next(ResponseEntity.ok().body("jai shree ram"));
			   }
		   });
	}
	
}
