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
					.log();
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
