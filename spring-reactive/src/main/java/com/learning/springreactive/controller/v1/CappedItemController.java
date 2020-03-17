package com.learning.springreactive.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import com.learning.springreactive.document.CappedItem;
import com.learning.springreactive.repository.CappedItemReactiveRepository;

import reactor.core.publisher.Flux;

@RestController
public class CappedItemController {

	@Autowired
	private CappedItemReactiveRepository cappedItemReactiveRepository;
	
	@GetMapping(value = "/v1/cappedItems" , produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<CappedItem> findAllCappedItems(){
		return cappedItemReactiveRepository.findItemsBy()
											.log("findAllCappedItems: Streaming item ");
			
	}

}
