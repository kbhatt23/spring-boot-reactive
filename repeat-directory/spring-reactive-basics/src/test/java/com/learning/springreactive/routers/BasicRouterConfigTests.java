package com.learning.springreactive.routers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

//since it is calling rest api we need context
@RunWith(SpringRunner.class)
//it imports only controller and rest controllers
//@WebFluxTest
@SpringBootTest
@AutoConfigureWebTestClient
public class BasicRouterConfigTests {

	@Autowired
	private WebTestClient client;
	
	@Test
	public void basicRoute() {
		Flux<Integer> responseBody = client.get().uri("/functional/ints")
				.accept(MediaType.APPLICATION_STREAM_JSON)
				.exchange()
				.expectStatus().isOk()
				.returnResult(Integer.class)
				.getResponseBody();
		
		StepVerifier.create(responseBody)
					.expectSubscription()
					.expectNext(1,2,3,4,5)
					.verifyComplete();
			
	}
	
	@Test
	public void basicRouteMono() {
		Integer expected = new Integer(108);
		client.get().uri("/functional/int")
				.accept(MediaType.APPLICATION_STREAM_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Integer.class)
				.consumeWith(entity -> 
					assertEquals(expected, entity.getResponseBody())
				);
		
	}
}
