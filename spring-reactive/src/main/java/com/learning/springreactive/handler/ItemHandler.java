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
					flatMap(requestItem ->{
						return ServerResponse.created(URI.create("/"))
								.contentType(MediaType.APPLICATION_STREAM_JSON)
								.body(itemReactiveRepository.save(requestItem),Item.class)
						;
						
					});
					
					
					
	}
}
