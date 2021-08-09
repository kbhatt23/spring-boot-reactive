package com.learning.products_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.products_service.dtos.InventoryDTO;
import com.learning.products_service.services.InventoryService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

	
	@Autowired
	private InventoryService inventoryService;
	
	@GetMapping(path = "/events",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	Flux<InventoryDTO> pushInventoryEvents(){
		return inventoryService.findInventoryEvents();
	}
	@PostMapping
	public Mono<ResponseEntity<InventoryDTO>> create(@RequestBody Mono<InventoryDTO> requestMono){
		return inventoryService.createInventory(requestMono);
	}
}
