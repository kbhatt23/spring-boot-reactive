package com.learning.springwebfluxdemo.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.learning.springwebfluxdemo.dtos.MathResponseDTO;
import com.learning.springwebfluxdemo.dtos.MultiplyRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/maths-client")
public class MathWebClientController {

	@Autowired
	private WebClient mathWebClient;
	
	@GetMapping("/squareRoot/{number}")
	public Mono<MathResponseDTO> squareRoot(@PathVariable("number") int number) {
		
		return mathWebClient.get()
		              .uri("/squareRoot/{number}", number)
		              //never use this, can not expect everywhere to update header , cross cutting concern
		             // .headers(headers -> headers.setBasicAuth("kbhatt23", "kanishklikesprogramming"))
		              
		              //custom header
		              .header("custom-key", "custom-val")
		              //.attribute("auth-type", "basic")
		              .attribute("auth-type", "bearer")
		              .retrieve()
		              .bodyToMono(MathResponseDTO.class)
		              .log("MathWebClientController.squareRoot(): ")
		              ;
	}
	
	@GetMapping(path = "/numberTable/{number}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<MathResponseDTO> numberTable(@PathVariable("number") int number) {
			return mathWebClient.get()
					    .uri("/numberTable/{number}", number)
					    .retrieve()
					    .bodyToFlux(MathResponseDTO.class)
					    .log("MathWebClientController.numberTable(): ")
					    ;
	}
	
	@PostMapping(path = "/multiply", produces = MediaType.TEXT_EVENT_STREAM_VALUE )
	public Mono<MathResponseDTO> multipleTwoNumbers(@RequestBody Mono<MultiplyRequest> request){
		return mathWebClient.post()
				   .uri("/multiply")
				   .body(request, MultiplyRequest.class)
				   //.bodyValue(request)
				   .retrieve()
				   .bodyToMono(MathResponseDTO.class)
				   .log("MathWebClientController.multipleTwoNumbers(): ")
				   ;	   
	}
	
	@GetMapping("/error/{input}")
	public Mono<String> handleError(@PathVariable int input){
	return	mathWebClient.get()
				  .uri("/error/{input}",input)
				  .retrieve()
				  .bodyToMono(String.class)
				  .onErrorResume(error -> {System.out.println("handleError.error occurred "+error); return Mono.just("jai lekshmi narayan");})
				  ;
				  
		
	}
	
}
