package com.learning.springreactive.inventory_reactive.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.springreactive.inventory_reactive.documents.InventoryItemCapped;
import com.learning.springreactive.inventory_reactive.repositories.InventoryItemCappedRepository;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/v1/stream")
public class CappedItemController {

	@Autowired
	private InventoryItemCappedRepository repo;
	
	@GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<InventoryItemCapped> stream(){
		return repo.findItemsBy();
		
	}
	
}
