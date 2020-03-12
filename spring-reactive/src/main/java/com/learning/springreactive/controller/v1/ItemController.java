package com.learning.springreactive.controller.v1;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.springreactive.document.Item;
import com.learning.springreactive.repository.ItemReactiveRepository;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/v1/items")
public class ItemController {

	@Autowired
	private ItemReactiveRepository repository;
	
	@GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Item> findAll(){
		return repository.findAll()
				.delayElements(Duration.ofSeconds(1))
				.log();
	}
}
