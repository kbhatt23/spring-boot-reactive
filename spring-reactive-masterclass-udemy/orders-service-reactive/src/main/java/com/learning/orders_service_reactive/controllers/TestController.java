package com.learning.orders_service_reactive.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.orders_service_reactive.dtos.ProductDTO;
import com.learning.orders_service_reactive.dtos.UserDTO;
import com.learning.orders_service_reactive.dtos.UserTransactionDTO;
import com.learning.orders_service_reactive.service.ProductClientService;
import com.learning.orders_service_reactive.service.UserClientService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/test")
public class TestController {

	
	@Autowired
	private ProductClientService productClientService;
	
	@Autowired
	private UserClientService userClientService;
	
	@GetMapping("/products/{productID}")
	public Mono<ProductDTO> testPRoduct(@PathVariable String productID){
		return productClientService.findProduct(productID);
	}
	
	@PostMapping(path = "/users/debit" ,produces = MediaType.TEXT_EVENT_STREAM_VALUE )
	public Mono<UserTransactionDTO> testDebit(@RequestBody Mono<UserTransactionDTO> request){
		return userClientService.debitAmount(request);
	}
	
	@GetMapping("/users/{userID}")
	public Mono<UserDTO> testPRoduct(@PathVariable long userID){
		return userClientService.findUser(userID);
	}
	
	@GetMapping(path = "/users" , produces =  MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<UserDTO> testAllUsers(){
		return userClientService.findAllUsers();
	}
	
	@GetMapping(path = "/products" , produces =  MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ProductDTO> testAllProducts(){
		return productClientService.findAllProducts();
	}
	
	
}
