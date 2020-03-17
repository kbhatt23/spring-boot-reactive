package com.learning.springreactive.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.learning.springreactive.document.CappedItem;
import com.learning.springreactive.repository.CappedItemReactiveRepository;

import reactor.core.publisher.Mono;

@Component
public class CappedItemHandler {

	@Autowired
	private CappedItemReactiveRepository cappedItemReactiveRepository;
	
	public Mono<ServerResponse> handleCappedItems(ServerRequest serverRequest){
		
		return ServerResponse.ok()
						.contentType(MediaType.APPLICATION_STREAM_JSON)
						.body(cappedItemReactiveRepository.findItemsBy(), CappedItem.class)
						;
	}
}
