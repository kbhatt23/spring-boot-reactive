package com.learning.springreactive.controllers;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.learning.springreactive.BaseTest;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
//picks only controller and restcontroller , service clases are ignored
@WebFluxTest
public class SelfLEarningControllerTest extends BaseTest{

	@Autowired
	private WebTestClient client;
	
	@Test
	public void testStrings() {
		//this line will be non blocking, however stepverifier will be blocking
		Flux<String> responseBody = client.get().uri("/learning/test")
				.accept(MediaType.APPLICATION_STREAM_JSON)
				.exchange()
				.expectStatus().isOk()
				.returnResult(String.class)
				.getResponseBody();
		StepVerifier.create(responseBody)
					.expectSubscription()
					.expectNext("sita-ramradhe-krishnauma-shankar")
					.verifyComplete();
	}
	@Test
	public void testInts() {
		Flux<Integer> responseBody = client.get().uri("/learning/test-int")
				.accept(MediaType.APPLICATION_STREAM_JSON)
				.exchange()
				.expectStatus().isOk()
				.returnResult(Integer.class)
				.getResponseBody();
		System.out.println("step verifier started");
		StepVerifier.create(responseBody)
					.expectSubscription()
					.expectNext(1)
					.expectNext(2)
					.expectNext(3)
					.expectNext(4,5)
					.verifyComplete();
	}
	
	@Test
	public void testIntsWithoutStepVerifier() {
	 client.get().uri("/learning/test-int")
				.accept(MediaType.APPLICATION_STREAM_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(Integer.class)
				.hasSize(5);
	}
	//this approach is fine only for finite stream as we have collection of stream to list<int>
	
	@Test
	public void testIntsWithoutStepVerifier_2() {
		//blocking call -> finie for finite stram
	 List<Integer> responseBody = client.get().uri("/learning/test-int")
				.accept(MediaType.APPLICATION_STREAM_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(Integer.class)
				.returnResult()
				.getResponseBody();
	 List<Integer> expectedList = Arrays.asList(1,2,3,4,5);
	 assertEquals(expectedList, responseBody);
		}
	
	//below is bad approach for infinite stream
//	@Test
//	public void infinite_bad() {
//		//blocking call -> finie for finite stram
//	 List<Integer> responseBody = client.get().uri("/learning/infinite")
//				.accept(MediaType.APPLICATION_STREAM_JSON)
//				.exchange()
//				.expectStatus().isOk()
//				.expectBodyList(Integer.class)
//				.returnResult()
//				.getResponseBody();
//	 List<Integer> expectedList = Arrays.asList(1,2,3,4,5);
//	 assertEquals(expectedList, responseBody);
//		}
	
	@Test
	public void infinite() {
		//we have to use flux return type
		//if we try to collect in for of list using above option than since it is infinite stream it wont work
	 Flux<Integer> responseBody = client.get().uri("/learning/infinite-cold")
				.accept(MediaType.APPLICATION_STREAM_JSON)
				.exchange()
				.expectStatus().isOk()
				.returnResult(Integer.class)
				.getResponseBody()
				;
	 //completely non blocking and stream keep one mitting the data inifnitely
	 //stepverifier can help do back pressure support in testing
	 StepVerifier.create(responseBody)
	 			 .expectSubscription()
	 			 .expectNext(0,1,2,3,4,5)
	 			 .thenCancel()
	 			 .verify()
	 			;
	}
	
	@Test
	public void monoTest() {
		Integer expected = new Integer(108);
		client.get().uri("/learning/mono-int")
					.accept(MediaType.APPLICATION_STREAM_JSON)
					.exchange()
					.expectStatus().isOk()
					.expectBody(Integer.class)
					.consumeWith(entityList ->
							assertEquals(expected, entityList.getResponseBody())
					);
	}
	
	
}
