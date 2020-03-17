package com.learning.springreactive.handler;

import java.time.Duration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.learning.springreactive.document.CappedItem;
import com.learning.springreactive.repository.CappedItemReactiveRepository;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@DirtiesContext
public class CappedITemHandlerTest {
	

	@Autowired
	private MongoOperations mongoOperations;
	
	@Autowired
	private CappedItemReactiveRepository cappedItemReactiveRepository;
	
	@Autowired
	private WebTestClient webTestclient;

	@Before
	public void setupData() {
		//every time removeing old collection and creating new
		mongoOperations.dropCollection(CappedItem.class);
		mongoOperations.createCollection(CappedItem.class, CollectionOptions
				.empty().maxDocuments(20)
				.size(20000).capped());
		
		Flux.interval(Duration.ofMillis(500))
		    //taking only 5 entries form o--4
		    .take(5)
			.map(l -> new CappedItem(null, "Test description "+l, 101.10+l))
			.flatMap(cappedItemReactiveRepository::save)
			.doOnNext(entry -> System.out.println("setupData : saved capped item "+entry))
			.blockLast()
			;
	}
	
	@Test
	public void testCappedITemStream() {
		System.out.println("started test testCappedITemStream");
		
		Flux<CappedItem> resulFluxCappedITems = webTestclient.get().uri("/v2/cappedItems")
						.accept(MediaType.APPLICATION_STREAM_JSON)
						.exchange()
						.returnResult(CappedItem.class)
						.getResponseBody();
		
		StepVerifier.create(resulFluxCappedITems)
				.expectSubscription()
				.expectNextCount(5)
				//take will force to cancel and never complete or on error
				.thenCancel()
				.verify();
		
	}
}
