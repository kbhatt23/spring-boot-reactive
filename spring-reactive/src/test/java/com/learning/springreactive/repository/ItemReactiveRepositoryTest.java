package com.learning.springreactive.repository;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.learning.springreactive.document.Item;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

//instead of calling springboottest or webflux test using below
//this will import only mondo related dependecies and autowite will be active
//for those components only eg: repostiory extends reactivemongorepostiry
@DataMongoTest
@RunWith(SpringRunner.class)
//also in test class embeded reactive mongo db will be used

public class ItemReactiveRepositoryTest {

	@Autowired
	ItemReactiveRepository itemReactiveRepository;
	
	List<Item> itemsList = Arrays.asList(new Item(null, "samsung tv", 199.0)
			,new Item(null, "lg tv", 201.0),
			new Item(null, "lenovo laptop", 189.0)
			,new Item("fake", "fake device", 189.0)
			);
	
	@Before
	public void beforeAll() {
		itemReactiveRepository.deleteAll()
						.thenMany(Flux.fromIterable(itemsList))
						.flatMap(itemReactiveRepository::save)
						.doOnNext(entry -> {
							System.out.println("entry inserted "+entry);
						})
						//blocking so that after this the data is consistent for other tests
						.blockLast();
	}
	
	@Test
	public void testAllItems() {
		System.out.println("start test testAllItems");
		Flux<Item> allITemsFlux = itemReactiveRepository.findAll();
		StepVerifier.create(allITemsFlux)
					.expectSubscription()
					.expectNextCount(4)
					.verifyComplete();
		
		
	}
	@Test
	public void testById() {
		System.out.println("start test testById");
		
		StepVerifier.create(itemReactiveRepository.findById("fake"))
						.expectSubscription()
						//below is working as equasls method is in place
						//logically if all properties value is same two objects are equal even though == will be false
						//.expectNext(new Item("fake", "fake device", 189.0))
						.expectNextMatches(entry -> entry.getId().equals("fake"))
						.verifyComplete();
		
		
	}
	
	@Test
	public void testByDescription() {
		System.out.println("start test testById");
		
		StepVerifier.create(itemReactiveRepository.findByDescriprion("samsung tv"))
					.expectSubscription()
					.expectNextMatches(item -> item.getDescriprion().equals("samsung tv"))
					.expectComplete()
					.verify();
		
	}
	
	@Test
	public void testInsert() {
		System.out.println("start test testInsert");
		
		Mono<Item> itemInserted = itemReactiveRepository.save(new Item(null, "inserted item", 999.9))
											.doOnNext(item -> System.out.println("king inserted item"+ item) );
		StepVerifier.create(itemInserted)
					.expectSubscription()
					.expectNextMatches(item -> item.getDescriprion().equals("inserted item"))
					.expectComplete()
					.verify();
		
	}
	
	@Test
	public void testUpdateByDescription() {
		System.out.println("start test testUpdateByDescription");
		Flux<Item> updatedFlux = itemReactiveRepository.findByDescriprion("samsung tv")
		//flatmap is used when input is Flux<Flux<Item1>> and we want Flux<Ite2>
		//map is used when input is Flux<Item1> and convert to Flux<Item2>
								.flatMap(item -> {
									item.setPrice(333.9);
									return itemReactiveRepository.save(item);
								});
		
		StepVerifier.create(updatedFlux)
						.expectSubscription()
						.expectNextMatches(item -> item.getPrice()==333.9)
						.verifyComplete();
		
	}
	
	@Test
	public void testdeleteByDescription() {
		System.out.println("start test testdeleteByDescription");
//	Flux<Item> deletedFlux =    itemReactiveRepository.findByDescriprion("samsung tv")
	//							.flatMap(itemReactiveRepository::delete)
		//						.flatMap(item ->{
			//						return itemReactiveRepository.findByDescriprion("samsung tv");
				//				})
					//			;
	
		Flux<Item> deletedFlux =    itemReactiveRepository.findByDescriprion("samsung tv")
				.flatMap(itemReactiveRepository::delete)
				.thenMany(itemReactiveRepository.findByDescriprion("samsung tv"))
				;
		StepVerifier.create(deletedFlux)
					.expectSubscription()
					//.expectNextCount(0)
				/*
				 * .expectNextMatches(item ->{ System.out.println("item after delte is "+item);
				 * boolean isValid= true; if(item != null &&
				 * item.getDescriprion().equals("samsung tv")) { isValid = false;
				 * 
				 * } return isValid; })
				 */
					.verifyComplete();
		
	}
}
