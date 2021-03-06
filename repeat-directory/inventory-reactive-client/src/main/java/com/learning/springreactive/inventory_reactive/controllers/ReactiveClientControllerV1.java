package com.learning.springreactive.inventory_reactive.controllers;

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

import com.learning.springreactive.inventory_reactive.documents.InventoryItem;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//use retrieve to call v1 apis
//other eg : /v1/exchange , /v2/retrive, /v2/exchange
@RestController
@RequestMapping("/v1/retrieve/client")
public class ReactiveClientControllerV1 {

	private WebClient client  = WebClient.create("http://localhost:8081/v1/inventory");
	
	@GetMapping
	public Flux<InventoryItem> findAll(){
		
		return client.get().accept(MediaType.APPLICATION_JSON_UTF8)
				    .retrieve()
				    .bodyToFlux(InventoryItem.class)
				    .retry(2)
				    .onErrorResume(e ->{System.out.println("findAll():error occurred "+e); return Flux.empty();})
				    ;
	}
	
	@GetMapping("/{inventoryId}")
	public Mono<ResponseEntity<InventoryItem>> findById(@PathVariable String inventoryId){
		
		return client.get()
				.uri("/{inventoryId}",inventoryId)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				    .retrieve()
				    .bodyToMono(InventoryItem.class)
				    .map(item -> new ResponseEntity<>(item,HttpStatus.OK))
					.defaultIfEmpty(new ResponseEntity<>(null,HttpStatus.NO_CONTENT))
					.retry(2)
					 .onErrorResume(e ->{System.out.println("findAll():error occurred "+e); return Mono.just(new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR));})
				    ;
	}
	
	@PostMapping
	public Mono<ResponseEntity<InventoryItem>> createItem(@RequestBody Mono<InventoryItem> itemMono) {
		
		return client.post().accept(MediaType.APPLICATION_JSON_UTF8)
					 .contentType(MediaType.APPLICATION_JSON_UTF8)
					 .body(itemMono, InventoryItem.class)
					 .retrieve()
					 .bodyToMono(InventoryItem.class)
					 .map(item -> new ResponseEntity<>(item,HttpStatus.CREATED))
				     .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
					 
					 
	}
	
	@PutMapping("/{inventoryId}")
	public Mono<ResponseEntity> update(@PathVariable String inventoryId, @RequestBody Mono<InventoryItem> itemMono) {
		return client.put().uri("/{inventoryId}",inventoryId)
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.body(itemMono, InventoryItem.class)
					.retrieve()
					.bodyToMono(ResponseEntity.class)
					;
	}
	
	@DeleteMapping("/{inventoryId}")
	public Mono<ResponseEntity> delete(@PathVariable String inventoryId) {
		
		return client.delete().uri("/{inventoryId}",inventoryId)
		.accept(MediaType.APPLICATION_JSON_UTF8)
		.retrieve()
		.bodyToMono(ResponseEntity.class);
	}
}
