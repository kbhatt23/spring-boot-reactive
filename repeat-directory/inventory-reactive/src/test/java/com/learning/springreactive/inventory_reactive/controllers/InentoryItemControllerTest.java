package com.learning.springreactive.inventory_reactive.controllers;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.learning.springreactive.inventory_reactive.documents.InventoryItem;
import com.learning.springreactive.inventory_reactive.repositories.InventoryItemRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

//webfluxtest -> loads only controllers, but if controller is injecting service/repository/co,poenent it wont work
//load everything from context
@SpringBootTest
@AutoConfigureWebTestClient
@RunWith(SpringRunner.class)
@DirtiesContext
public class InentoryItemControllerTest {

	@Autowired
	private WebTestClient client;
	
	private List<InventoryItem> items = Arrays.asList(new InventoryItem("sita-ram", "jai sita ram"),
			new InventoryItem("radhe-krishna", "jai radhe krishna"),
			new InventoryItem("ABC","radhe-krishna", "jai radhe krishna")//this time id will not be auto generated
			);
	
	//thats why loading springboot test load everything from main context
	@Autowired
	private InventoryItemRepository repository;
	
	@Before
	public void setup() {
		repository.deleteAll()
				  .thenMany(Flux.fromIterable(items))
				  .flatMap(repository::save)
				  .doOnNext(item -> System.out.println("setup: Created Item "+item))
				  .blockLast()
				  ;
	}
	
	@Test
	public void testSuccessCreation() {
		InventoryItem inventoryItem = new InventoryItem("ramduta-hanuman", "jai shree ram jai ramduta hanuman");
		client.post().uri("/v1/inventory")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.body(Mono.just(inventoryItem), InventoryItem.class)
					.exchange()
					.expectStatus().isCreated()
					;
	
	}
	
	@Test
	public void testFailureCreation() {
		InventoryItem inventoryItem = new InventoryItem("naklee-id","naklee", "naklee tak bole jai shree ram");
		client.post().uri("/v1/inventory")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.body(Mono.just(inventoryItem), InventoryItem.class)
					.exchange()
					.expectStatus().isBadRequest()
					;
	
	}
	@Test
	public void testFindAll() {
		Flux<InventoryItem> responseBody = client.get().uri("/v1/inventory")
		.exchange()
		.expectStatus().isOk()
		.returnResult(InventoryItem.class)
		.getResponseBody();
		;
		
		StepVerifier.create(responseBody)
					.expectSubscription()
					.expectNextCount(3)
					.verifyComplete()
					;
	}	
	
	@Test
	public void testFindByIdSuccess() {
		client.get().uri("/v1/inventory/ABC")
					 .accept(MediaType.APPLICATION_JSON_UTF8)
					 .exchange()
					 .expectStatus().isOk()
					 .expectBody(InventoryItem.class)
					 .consumeWith(entity -> assertEquals("ABC", entity.getResponseBody().getId()));
	}
	
	@Test
	public void testFindByIdFailure() {
		client.get().uri("/v1/inventory/nakleeid")
					 .accept(MediaType.APPLICATION_JSON_UTF8)
					 .exchange()
					 .expectStatus().isNoContent();
	}
	
	@Test
	public void testupdateItemSuccess() {
		InventoryItem updateItem = new InventoryItem("ABC","radhekrishna", "jai radhe krishna says everyone");
		client.put().uri("/v1/inventory/ABC")
					 .accept(MediaType.APPLICATION_JSON_UTF8)
					 .contentType(MediaType.APPLICATION_JSON_UTF8)
					 .body(Mono.just(updateItem), InventoryItem.class)
					 .exchange()
					 .expectStatus().isOk();
	}
	
	@Test
	public void testupdateItemFailure() {
		InventoryItem updateItem = new InventoryItem("ABCD","radhekrishna", "jai radhe krishna says everyone");
		client.put().uri("/v1/inventory/ABCD")
					 .accept(MediaType.APPLICATION_JSON_UTF8)
					 .contentType(MediaType.APPLICATION_JSON_UTF8)
					 .body(Mono.just(updateItem), InventoryItem.class)
					 .exchange()
					 .expectStatus().isBadRequest();
	}
	
	//@Test
	public void testDelteItemSuccess() {
		client.delete().uri("/v1/inventory/ABC")
					 .accept(MediaType.APPLICATION_JSON_UTF8)
					 .exchange()
					 .expectStatus().isNoContent();
	}
}
