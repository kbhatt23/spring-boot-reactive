package com.learning.springreactive.handler;

import java.net.URI;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.learning.springreactive.document.Item;
import com.learning.springreactive.repository.ItemReactiveRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ItemHandler {
	@Autowired
	private ItemReactiveRepository itemReactiveRepository;

	//server request will be useful in post, put and other type where imput is
	//presented from postman or web client
	public Mono<ServerResponse> findAllHandler(ServerRequest serverRequest){
		return ServerResponse.ok()
							 .contentType(MediaType.APPLICATION_STREAM_JSON)
							 .body(itemReactiveRepository.findAll()
									 .delayElements(Duration.ofSeconds(1))
									 , Item.class);
	}
	
	public Mono<ServerResponse> findById(ServerRequest serverRequest){
		String itemID = serverRequest.pathVariable("itemId");
		if(StringUtils.isEmpty(itemID)) {
			return ServerResponse.badRequest()
					.build()
					;
		}
		return itemReactiveRepository.findById(itemID)
									.flatMap(itemMono -> {
										return ServerResponse.ok()
										.contentType(MediaType.APPLICATION_STREAM_JSON)
										.body(Mono.just(itemMono), Item.class);
													
									})
									.switchIfEmpty(ServerResponse.notFound()
											.build()
											)
									;
		
	}
	
	public Mono<ServerResponse> createItem(ServerRequest serverRequest){
		Mono<Item> requestBody = serverRequest.bodyToMono(Item.class);
		
		return requestBody.
				//filtering already existing items
				//as this method is only for new creation only
					filter(requestItem -> StringUtils.isEmpty(requestItem.getId()))
					//assuming if id is not present then it means blindly we can create new entry inDB with new generate ID
					.flatMap(requestItem ->{
						return ServerResponse.created(URI.create("/"))
								.contentType(MediaType.APPLICATION_STREAM_JSON)
								.body(itemReactiveRepository.save(requestItem),Item.class)
						;
						
					})
					.switchIfEmpty(ServerResponse.badRequest().build())
					;
	}
	
	public Mono<ServerResponse> deleteById(ServerRequest serverRequest){
		String itemID = serverRequest.pathVariable("itemId");
		if(StringUtils.isEmpty(itemID)) {
			return ServerResponse.badRequest().build();
		}
		
		return itemReactiveRepository.deleteById(itemID)
						.log()
						.then(ServerResponse.noContent().build())
						.switchIfEmpty(ServerResponse.badRequest().build())
							  ;
		
	}
	
	public Mono<ServerResponse> updateItem(ServerRequest serverRequest){
		Mono<Item> itemMono = serverRequest.bodyToMono(Item.class);
		
		return itemMono.
				//filtering items which do not exist
				//if id do not exist how can we update
					filter(requestItem -> !StringUtils.isEmpty(requestItem.getId()))
					//assuming if id is not present then it means blindly we can create new entry inDB with new generate ID
					.flatMap(requestItem ->{
						return ServerResponse.ok()
								.contentType(MediaType.APPLICATION_STREAM_JSON)
								.body(itemReactiveRepository.save(requestItem),Item.class)
						;
						
					})
					.switchIfEmpty(ServerResponse.badRequest().build())
					;
				
		
	}
	
	public Mono<ServerResponse> findAllWithException(ServerRequest serverRequest){
		//make it true to test exception scenario
		boolean sendError =false;
		if(sendError)
		throw new RuntimeException("handler ke router ke excpetion me bhee ram");
		

		return ServerResponse.ok()
							 .contentType(MediaType.APPLICATION_STREAM_JSON)
							 .body(itemReactiveRepository.findAll()
									 .delayElements(Duration.ofSeconds(1))
									 , Item.class);
	
	}
}
