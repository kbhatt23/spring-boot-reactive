package com.learning.springreactive.inventory_reactive;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.learning.springreactive.inventory_reactive.documents.InventoryItem;
import com.learning.springreactive.inventory_reactive.repositories.InventoryItemRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@DataMongoTest
public class BasicMpongoOperationTest extends BaseTest{

	@Autowired
	private InventoryItemRepository repository;
	
	private List<InventoryItem> items = Arrays.asList(new InventoryItem("sita-ram", "jai sita ram"),
			new InventoryItem("radhe-krishna", "jai radhe krishna"),
			new InventoryItem("ABC","radhe-krishna", "jai radhe krishna")//this time id will not be auto generated
			);
	
	@Before
	public void setup() {
		//remove exisitng data and insert new data everytime -> saves data corruption
		repository.deleteAll()
				 .thenMany(Flux.fromIterable(items))
				 .flatMap(repository::save)
				 .doOnNext(item -> System.out.println("Created Item "+item))
				 .blockLast()
				 ;
	}
	
	@Test
	public void findAllItem() {
		//empty case
		StepVerifier.create(repository.findAll().log())
					 .expectSubscription()
					 .expectNextCount(3)
					 .verifyComplete();
		
		;					
	}
	
	@Test
	public void findById() {
		String expectedID = "ABC";
		StepVerifier.create(repository.findById(expectedID).log())
					.expectSubscription()
					.expectNextMatches(item -> item.getId().equals(expectedID))
					.verifyComplete()
					;
		;
	}
	
	@Test
	public void findByDescriptionContains() {
		String expectedDescriptionContains = "jai";
		
		StepVerifier.create(repository.findByDescriptionContains(expectedDescriptionContains).log())
					.expectSubscription()
					//all returned items must contain expected string in descriptionm
					.expectNextMatches(item -> item.getDescription().contains(expectedDescriptionContains))
					//since we are returning bounded items on complete even will not be fiered
					.thenCancel()
					.verify()
			
		;
	}
	
	@Test
	public void testInsert() {
		InventoryItem item = new InventoryItem("fake", "fake-king", "fake baba se bacho");
		Mono<InventoryItem> itemMono = Mono.just(item)
										.flatMap(i -> repository.save(i))
										.then(repository.findById(item.getId()))
										.log()
										;
				
		StepVerifier.create(itemMono)
					.expectNextMatches(a -> a.getId().equals(item.getId()))
					.verifyComplete()
					
		;
		
		
	}
	
	@Test
	public void testUpdate() {
		String expectedID = "ABC";
		String updatedName = "radhe-krishna-updated";
		Mono<InventoryItem> updatedMono = repository.findById(expectedID)
				.map(item -> {item.setName(updatedName); return item;})
				  .flatMap(repository::save
				  ).then(repository.findById(expectedID));
			
		;
		StepVerifier.create(updatedMono)
					.expectSubscription()
					.expectNextMatches(item -> item.getId().equals(expectedID) && updatedName.equals(item.getName()))
					.verifyComplete()
					;
		
	}
	
	@Test
	public void testDelete() {
		String expectedID = "ABC";
		Mono<InventoryItem> monoRemoved = repository.findById(expectedID)
				 .flatMap(repository::delete)
				 .then(repository.findById(expectedID))
				 .log()
				 ;
		
		//stepverifier is blocking aleady
		StepVerifier.create(monoRemoved)
					.expectSubscription()
					.expectNextCount(0)
					.verifyComplete();
				 
	}
	
}
