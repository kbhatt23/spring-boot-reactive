package com.learning.products_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.learning.products_service.documents.InventoryDocument;
import com.learning.products_service.documents.ProductDocument;
import com.learning.products_service.dtos.InventoryDTO;
import com.learning.products_service.dtos.ProductDTO;
import com.learning.products_service.repositories.InventoryRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class InventoryService {

	@Autowired
	private InventoryRepository inventoryRepository;
	
	public Flux<InventoryDTO> findInventoryEvents() {
		return inventoryRepository.findItemsBy()
					.map(InventoryDTO :: new);
	}
	
	public Mono<ResponseEntity<InventoryDTO>> createInventory(Mono<InventoryDTO> requestMono){
		//start from pipeline
		return requestMono
				.doOnNext(input -> log.info("createInventory: input requested "+input))
				.filter(request -> StringUtils.isEmpty(request.getId()) && !StringUtils.isEmpty(request.getProductId()))
				.doOnNext(input -> log.info("createInventory: input filtered "+input))
					.map(InventoryDocument :: new)
					.flatMap(inventoryRepository :: save)
					.map(InventoryDTO :: new)
					.doOnNext(output -> log.info("createInventory: final created data "+output))
					.map(saved -> new ResponseEntity<>(saved, HttpStatus.CREATED))
					.switchIfEmpty(Mono.error(() -> new IllegalArgumentException("create: can not pass id while inserting data")))
					;
			   
	}
	
}
