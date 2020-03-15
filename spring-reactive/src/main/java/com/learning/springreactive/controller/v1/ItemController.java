package com.learning.springreactive.controller.v1;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.learning.springreactive.document.Item;
import com.learning.springreactive.repository.ItemReactiveRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/items")
public class ItemController {

	@Autowired
	private ItemReactiveRepository repository;

	@GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Item> findAll() {
		return repository.findAll().delayElements(Duration.ofSeconds(1)).log()
				;
	}

	// better to use the next method, as
	// in case data is not present we shud send 404 not found status
	// @GetMapping(value = "/{itemId}",produces =
	// MediaType.APPLICATION_STREAM_JSON_VALUE)
	// public Mono<Item> findById(@PathVariable("itemId") String itemID){
	// this code is missing the condition what to do if entry is missing
	// return repository.findById(itemID)
	// .log()
	// ;

//	}

	@GetMapping(value = "/{itemId}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Mono<ResponseEntity<Item>> findById(@PathVariable("itemId") String itemID) {
		// this code is missing the condition what to do if entry is missing
		return repository.findById(itemID).log().map(monoItem -> {
			// takes mono<item> and converts to mono<responseEntity<Item>>
			return new ResponseEntity<>(monoItem, HttpStatus.OK);
		}).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));

	}

	@PostMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Mono<ResponseEntity<Item>> saveItem(@RequestBody Item item) {
		if (!StringUtils.isEmpty(item.getId())) {
			return Mono.fromSupplier(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
		}
		return repository.save(item).log().map(monoItem -> new ResponseEntity<>(item, HttpStatus.CREATED));
	}

	@DeleteMapping(value = "/{itemId}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Mono<ResponseEntity<Object>> deleteById(@PathVariable("itemId") String itemID) {
		if (StringUtils.isEmpty(itemID)) {
			return Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
		}
		return repository.deleteById(itemID).log().then(Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT)))
				// this never comes
				// so we must handle above
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

		// below code is acting simliraly
		// however performance is better in above
		/*
		 * return repository.findById(itemID) .log() .map(Item::getId)
		 * .flatMap(repository::deleteById) .then(Mono.just(new
		 * ResponseEntity<>(HttpStatus.NO_CONTENT))) .defaultIfEmpty(new
		 * ResponseEntity<>(HttpStatus.BAD_REQUEST)) ;
		 */

	}

	@PutMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Mono<ResponseEntity<Item>> updateItem(@RequestBody Item item) {
		if (StringUtils.isEmpty(item.getId())) {
			return Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
		}
		// if data exists
		return repository.findById(item.getId()).flatMap(itemMap -> repository.save(item))
				.map(itemMono -> new ResponseEntity<>(itemMono, HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
}
