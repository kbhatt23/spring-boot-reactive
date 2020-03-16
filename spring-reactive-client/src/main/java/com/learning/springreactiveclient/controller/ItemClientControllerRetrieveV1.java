package com.learning.springreactiveclient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.learning.springreactiveclient.document.Item;
import com.learning.springreactiveclient.exception.CustomExceptionResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/client/retrieve/items")
//contains inmplemetation to call v1 version of apis using retrieve
public class ItemClientControllerRetrieveV1 {

	@Autowired
	private WebClient webClient;
	
	@GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Item> retrieveAllItems() {
		
	return webClient.get().uri("/v1/items")
					.retrieve()
					.bodyToFlux(Item.class)
					.log()
					//this will be handled in controller advice
					//or we coould have returned custom status also if we wanted using onErrorResume
					//but will need controller advice and customexception response class
					.onErrorMap(error -> {
						System.out.println("retrieveAllItems: error occurred "+error.getMessage());
						return new RuntimeException("error occurred while calling find all api");
						})
					;
	}
	
	//error handling case
	//in above we do not know the Customer error response json and just get the error and printing that 
	//in below we can even get the custom erros response from third party
	@GetMapping(value = "/error",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Item> retrieveAllItemsException() {
		
	return webClient.get().uri("/v2/items")
					.retrieve()
					.onStatus(HttpStatus::is5xxServerError, clientResponse -> {
						Mono<CustomExceptionResponse> errorMono=clientResponse.bodyToMono(CustomExceptionResponse.class);
						return errorMono.flatMap(error -> {
							System.out.println("retrieveAllItemsException: error occurred "+error.getErrorMessage());
							throw new RuntimeException(error.getErrorMessage());
						});
					})
					//similiarly add errors for different status
					
					.bodyToFlux(Item.class)
					.log()
					//this will be handled in controller advice
					//or we coould have returned custom status also if we wanted using onErrorResume
					//but will need controller advice and customexception response class
					;
	}
	
	@GetMapping(value = "/{itemId}",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Mono<ResponseEntity<Item>> retrieveItemById(@PathVariable("itemId") String itemID) {
		
	return webClient.get().uri("/v1/items/"+itemID)
					.retrieve()
					.bodyToMono(Item.class)
					.map(item -> new ResponseEntity<>(item,HttpStatus.OK))
					.onErrorResume((error) -> {
						//we can check for error and handle as per that
						System.out.println("retrieveItemById: Exception occurred "+error.getMessage());
						return Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND));
					})
					.log();
	}
	
	@PostMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Mono<ResponseEntity<Item>> createItem(@RequestBody Item item){
		
		return webClient.post().uri("/v1/items/")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_STREAM_JSON)
						.body(Mono.just(item), Item.class)
						.retrieve()
						.bodyToMono(Item.class)
						.map(itemResponse -> new ResponseEntity<>(itemResponse, HttpStatus.CREATED))
						.log();
						
		
	}
	
	@PutMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Mono<ResponseEntity<Item>> updateItem(@RequestBody Item item){
		
		return webClient.put().uri("/v1/items")
					   .contentType(MediaType.APPLICATION_JSON)
					   .accept(MediaType.APPLICATION_STREAM_JSON)
					   .body(Mono.just(item), Item.class)
					   .retrieve()
					   .bodyToMono(Item.class)
					   .map(itemMono -> new ResponseEntity<>(itemMono, HttpStatus.OK));
	}
	
	@DeleteMapping(value = "/{itemId}" ,produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Mono<ResponseEntity<Object>> deleteById(@PathVariable("itemId") String itemID) {
		
		return webClient.delete().uri("/v1/items/"+itemID)
						  .accept(MediaType.APPLICATION_STREAM_JSON)
						  .retrieve()
						  .bodyToMono(Object.class)
						  .map(objMono -> new ResponseEntity<>(objMono, HttpStatus.NO_CONTENT));
	}
}
