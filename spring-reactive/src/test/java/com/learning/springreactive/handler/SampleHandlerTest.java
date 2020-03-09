package com.learning.springreactive.handler;

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

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class SampleHandlerTest {

	@Autowired
	WebTestClient webTestClient;
	
	@Test
	public void testFlux() {
		System.out.println("start test testFlux");
		Flux<Integer> resultFlux = webTestClient.get().uri("/functional/flux")
							.accept(MediaType.APPLICATION_JSON)
							.exchange()
							.expectStatus().isOk()
							.returnResult(Integer.class)
							.getResponseBody();
		
		StepVerifier.create(resultFlux)
					.expectSubscription()
					.expectNext(1,2,3,4)
					.verifyComplete();
	}
	
	@Test
	public void testFluxStream() {
		System.out.println("start test testFluxStream");
		Flux<Integer> resultFlux = webTestClient.get().uri("/functional/fluxStream")
							.accept(MediaType.APPLICATION_STREAM_JSON)
							.exchange()
							.expectStatus().isOk()
							.returnResult(Integer.class)
							.getResponseBody();
		
		StepVerifier.create(resultFlux)
					.expectSubscription()
					.expectNext(1,2,3,4)
					.verifyComplete();
	}
}
