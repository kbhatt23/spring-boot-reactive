package com.learning.springreactive.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.learning.springreactive.controller.exception.CustomExceptionResponse;
import com.learning.springreactive.document.Item;
import com.learning.springreactive.repository.ItemReactiveRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
public class ItemHandlerTest {


	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private ItemReactiveRepository itemReactiveRepository;

	// this will save data in test reactive mongo db which is embeded and not the
	// original one
	@Before
	public void setupData() {
		List<Item> itemsList = Arrays.asList(new Item(null, "samsung tv", 199.0), new Item(null, "lg tv", 201.0),
				new Item(null, "lenovo laptop", 189.0), new Item("fake", "fake device", 189.0));

		itemReactiveRepository.deleteAll().thenMany(Flux.fromIterable(itemsList)).flatMap(itemReactiveRepository::save)
				.doOnNext(item -> System.out.println("ItemControllerTest.setupData(): saving item " + item))
				.blockLast();
	}

	//this will be tested only in case error occurs
	@Test
	public void testfindAllWithException() {
		System.out.println("started test testfindAll");
		String expected = "handler ke router ke excpetion me bhee ram";
		webTestClient.get().uri("/v2/items").accept(MediaType.APPLICATION_STREAM_JSON).exchange()
				.expectStatus().is5xxServerError().expectBody(CustomExceptionResponse.class).
				consumeWith(itemResponse -> {
					CustomExceptionResponse res = itemResponse.getResponseBody();
					assertEquals(expected, res.getErrorMessage());
				});
				

	}
	
	@Test
	public void testfindAll() {
		System.out.println("started test testfindAll");

		Flux<Item> fluxItem = webTestClient.get().uri("/v2/items").accept(MediaType.APPLICATION_STREAM_JSON).exchange()
				.expectStatus().isOk().returnResult(Item.class).getResponseBody();
		StepVerifier.create(fluxItem).expectSubscription().expectNextCount(4).verifyComplete();

	}

	// this method test only happy flwo when id is found
	@Test
	public void testFindById() {
		System.out.println("started test testFindById");
		String idToFind = "fake";
		webTestClient.get().uri("/v2/items/" + idToFind).accept(MediaType.APPLICATION_STREAM_JSON).exchange()
				.expectStatus().isOk().expectBody(Item.class).consumeWith(item -> {
					assertEquals(idToFind, item.getResponseBody().getId());
				});
	}

	// this method test only negative flow when id is not found
	@Test
	public void testFindByIdNotFound() {
		System.out.println("started test testFindById");
		// id that is not present
		String idToFind = "1212n1h2";
		webTestClient.get().uri("/v2/items/" + idToFind).accept(MediaType.APPLICATION_STREAM_JSON).exchange()
				.expectStatus().isNotFound();
	}

	// as part of post the data itslef goes as mono and comes back as mono
	@Test
	public void testCreateSuccess() {
		System.out.println("started test testCreateSuccess");
		Item item = new Item(null, "test tv", 10.10);
		double expectedPRice = 10.10;
		webTestClient.post().uri("/v2/items/").contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(Mono.just(item), Item.class).exchange().expectStatus().isCreated().expectBody(Item.class)
				.consumeWith(itemBody -> {
					Item realITem = itemBody.getResponseBody();
					assertTrue(realITem.getPrice() == expectedPRice);
				});
	}

	// as part of post the data itslef goes as mono and comes back as mono
	@Test
	public void testCreateFailure() {
		System.out.println("started test testCreateFailure");
		Item item = new Item("test mi id", "test tv", 10.10);
		webTestClient.post().uri("/v2/items/").contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(Mono.just(item), Item.class).exchange().expectStatus().isBadRequest();
	}

	@Test
	public void testDeleteById() {
		System.out.println("started test testDeleteById");
		String idToDelete = "fake";
		webTestClient.delete().uri("/v2/items/" + idToDelete).accept(MediaType.APPLICATION_STREAM_JSON).exchange()
				.expectStatus().isNoContent();
		;
	}

	@Test
	public void testUpdateItem() {
		System.out.println("started test testDeleteById");
		Double inputPrice = 999.99;
		Double expectedPRice = inputPrice;
		//if no update
		//Double expectedPRice = 189.0;
		
		Item item = new Item("fake", "new test data" , inputPrice);
		webTestClient.put().uri("/v2/items")
					 .accept(MediaType.APPLICATION_STREAM_JSON)
					 .contentType(MediaType.APPLICATION_JSON_UTF8)
					 .body(Mono.just(item), Item.class)
					 .exchange()
					 .expectStatus().isOk()
					 .expectBody(Item.class)
					 .consumeWith(itemBody ->{
						 	Item updateItem = itemBody.getResponseBody();
						 	assertEquals(expectedPRice, updateItem.getPrice());
					 });
						
	}
	

	@Test
	public void testUpdateItemEmptyId() {
		System.out.println("started test testUpdateItemEmptyId");
		Double inputPrice = 999.99;
		Double expectedPRice = inputPrice;
		//if no update
		//Double expectedPRice = 189.0;
		
		Item item = new Item("", "new test data" , inputPrice);
		webTestClient.put().uri("/v2/items")
					 .accept(MediaType.APPLICATION_STREAM_JSON)
					 .contentType(MediaType.APPLICATION_JSON_UTF8)
					 .body(Mono.just(item), Item.class)
					 .exchange()
					 .expectStatus().isBadRequest()
					 	;
	}

	@Test
	public void testUpdateItemWrongId() {
		System.out.println("started test testUpdateItemWrongId");
		Double inputPrice = 999.99;
		Double expectedPRice = inputPrice;
		//if no update
		//Double expectedPRice = 189.0;
		
		Item item = new Item("fakenotPresent", "new test data" , inputPrice);
		webTestClient.put().uri("/v2/items")
					 .accept(MediaType.APPLICATION_STREAM_JSON)
					 .contentType(MediaType.APPLICATION_JSON_UTF8)
					 .body(Mono.just(item), Item.class)
					 .exchange()
					 .expectStatus().isNotFound();
						
	}
	

}
