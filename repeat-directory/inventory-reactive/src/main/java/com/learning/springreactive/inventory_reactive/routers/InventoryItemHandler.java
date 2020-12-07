package com.learning.springreactive.inventory_reactive.routers;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.learning.springreactive.inventory_reactive.documents.InventoryItem;
import com.learning.springreactive.inventory_reactive.repositories.InventoryItemRepository;

import reactor.core.publisher.Mono;

@Component
public class InventoryItemHandler {
	
	@Autowired
	private InventoryItemRepository repository;

	public Mono<ServerResponse> handleGetAll(ServerRequest request){
	return 	ServerResponse.ok()
					 .contentType(MediaType.APPLICATION_JSON_UTF8)
					 .body(repository.findAll(), InventoryItem.class)
					 ;
	}
	
	public Mono<ServerResponse> handlefindByID(ServerRequest request){
		String inventoryId = request.pathVariable("inventoryId");
		
		return repository.findById(inventoryId)
				  .flatMap(item -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(item), InventoryItem.class))
				  .switchIfEmpty(ServerResponse.noContent().build())
		;
		}


	public Mono<ServerResponse> handleCreate(ServerRequest request){
		Mono<InventoryItem> inventoryItem = request.bodyToMono(InventoryItem.class);
		
		return inventoryItem
					.filter(item -> StringUtils.isEmpty(item.getId()))
					 .flatMap(repository::save)
					 .flatMap(item -> {
						 try {
							return ServerResponse.created(new URI("/v2/inventory/"+item.getId())).contentType(MediaType.APPLICATION_JSON_UTF8)
							 .body(Mono.just(item),InventoryItem.class)
							 ;
						} catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						 return null;
						 })
					 .switchIfEmpty(ServerResponse.badRequest().build())
					 
					 ;
		
		}
	
	public Mono<ServerResponse> handleUpdate(ServerRequest request){
		Mono<InventoryItem> inventoryItem = request.bodyToMono(InventoryItem.class);
		String inventoryId = request.pathVariable("inventoryId");
		
		return repository.findById(inventoryId)
		  .filter(item -> item.getId().equals(inventoryId))
		  .flatMap(item -> inventoryItem.map(newItem -> {
			  item.setDescription(newItem.getDescription());
			  item.setName(newItem.getName());
			  return item;
		  }))
		  .flatMap(repository::save)
		  .flatMap(item -> ServerResponse.ok().build())
		  .switchIfEmpty(ServerResponse.badRequest().build())
		  ;
		
	}
	
	public Mono<ServerResponse> handleDelete(ServerRequest request){
		String inventoryId = request.pathVariable("inventoryId");
		
		return 		repository.findById(inventoryId)
				  .filter(item -> item.getId().equals(inventoryId))
				  .flatMap(repository::delete)
				  .flatMap(res -> ServerResponse.noContent().build())
				  .switchIfEmpty(ServerResponse.badRequest().build())
		;
	}
	}


