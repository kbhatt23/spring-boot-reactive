package com.learning.springreactive.controller;

import static org.junit.Assert.assertEquals;

import java.time.Duration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

@RunWith(SpringRunner.class)
//below annotaoin helps provide client code to test reactive api end points
@WebFluxTest
public class FluxAndMonoControllerTest {

	@Autowired
	WebTestClient webTestClient;
	
	@Test
	public void testAprraoach1() {
		System.out.println("started test testAprraoach1");
		Flux<Integer> fluxInteger =  webTestClient.get().uri("/flux")
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.exchange()
					.expectStatus().isOk()
					.returnResult(Integer.class)
					.getResponseBody()
					;
		
		StepVerifier.create(fluxInteger)
						.expectSubscription()
						.expectNext(1,2,3,4,5)
						.verifyComplete();
	}
	
	
	@Test
	public void testAprraoach2() {
		System.out.println("started test testAprraoach2");
		Flux<Integer> fluxInteger =  webTestClient.get().uri("/fluxStream")
					.accept(MediaType.APPLICATION_STREAM_JSON)
					.exchange()
					.expectStatus().isOk()
					.returnResult(Integer.class)
					.getResponseBody()
					;
		
		StepVerifier.create(fluxInteger)
						.expectSubscription()
						.expectNext(1,2,3,4,5)
						.verifyComplete();
	}
	
	@Test
	public void testInfiniteColdStream() {
		System.out.println("started test testInfiniteColdStream");
		
		Flux<Integer> infiniteFlux = webTestClient.get().uri("/fluxInfiniteCold")
							.accept(MediaType.APPLICATION_STREAM_JSON)
							.exchange()
							.expectStatus().isOk()
							.returnResult(Integer.class)
							.getResponseBody();
		
		StepVerifier.create(infiniteFlux)
					.expectSubscription()
					.expectNext(0)
					.expectNext(1,2,3,4,5)
					//forecefully cancel the stream
					.thenCancel()
					//after cancel the complete ecent wont come, nor error event or next event
					.verify();
		
	}
	
	@Test
	public void testMono() {
		System.out.println("started test testMono");
		Integer expected  =1;
		webTestClient.get().uri("/mono")
							.accept(MediaType.APPLICATION_JSON_UTF8)
							.exchange()
							.expectStatus().isOk()
							.expectBody(Integer.class)
							.consumeWith(monoResultData -> {
								System.out.println("raghav bhakt hanuman ka data "+monoResultData.getResponseBody());
								assertEquals(expected, monoResultData.getResponseBody());
							});
	}
	
	@Test
	public void testMonoStream() {
		System.out.println("started test testMonoStream");
		Integer expected  =1;
		webTestClient.get().uri("/monoStream")
							.accept(MediaType.APPLICATION_STREAM_JSON)
							.exchange()
							.expectStatus().isOk()
							.expectBody(Integer.class)
							.consumeWith(monoResultData -> {
								System.out.println("raghav bhakt hanuman ka data "+monoResultData.getResponseBody());
								assertEquals(expected, monoResultData.getResponseBody());
							});
	}
	
}
