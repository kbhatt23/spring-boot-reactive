package com.learning.springreactive.inventory_reactive.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.springreactive.inventory_reactive.documents.InventoryItem;
import com.learning.springreactive.inventory_reactive.repositories.InventoryItemRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/inventory")
public class InentoryItemController {

	@Autowired
	private InventoryItemRepository repository;
	
	@PostMapping
	public Mono<ResponseEntity<InventoryItem>> createInventoryItem(@RequestBody Mono<InventoryItem> inventoryItemMono){
		//validate the inventory item
		//if id is provided as per contreact we do not process
		return inventoryItemMono.filter(item -> StringUtils.isEmpty(item.getId()))
					     .flatMap(repository::save)
					     .map(item -> new ResponseEntity<>(item,HttpStatus.CREATED))
					     .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST))
					     .log("createInventoryItem: ")
		;
	}
	
	@GetMapping
	public Flux<InventoryItem> findAll(){
		return repository.findAll()
				//.map(item -> new ResponseEntity<>(item,HttpStatus.OK))
				.log("findAll: ");
	}
	@GetMapping("/{inventoryId}")
	public Mono<ResponseEntity<InventoryItem>> findById(@PathVariable String inventoryId){
		return repository.findById(inventoryId)
				.map(item -> new ResponseEntity<>(item,HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(null,HttpStatus.NO_CONTENT))
				.log("findById: ");
	}
	
	@PutMapping("/{inventoryId}")
	public Mono<ResponseEntity<Object>> updateItem(@RequestBody InventoryItem inventoryItemMono, @PathVariable String inventoryId) {
		//mono of empoty in case item didi not exist
		return repository.findById(inventoryId)
				  .map(existing -> {existing.setDescription(inventoryItemMono.getDescription());
				  existing.setName((inventoryItemMono.getName()));
				  return existing;
				  })
				  .flatMap(repository::save)
				  .map(item -> new ResponseEntity<>(HttpStatus.OK))
				  .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST))
				 
		;
	}
	
	//remeber the server acts as subscriber and hence we must return flux/mono otherwise main thread will skip the steps without subvscriber
		//-> cold stream by default
	@DeleteMapping("/{inventoryId}")
	public Mono<ResponseEntity<Object>> delete(@PathVariable String inventoryId) {
		return repository.findById(inventoryId)
		.filter(item -> inventoryId.equals(item.getId()))
				  .flatMap(repository::delete)
				  .map(item -> new ResponseEntity<>(HttpStatus.NO_CONTENT))
				  //.defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST))
				  ;
		
	}
}
