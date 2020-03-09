package com.learning.springreactive.handler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
//not using @webfluxTest as it can not pick @componemt -> we can only use @controller and restcontroller only
//in functionla web we have @componetn for handler nad hance webfluxtest wont work
@SpringBootTest
@AutoConfigureWebTestClient
public class SampleMonoHandlerTest {

	@Autowired
	WebTestClient webTestClient;
	
	@Test
	public void testMono() {
		System.out.println("start test testMono");
		Integer expectedMono = 1;
		webTestClient.get().uri("/functional/mono")
							.accept(MediaType.APPLICATION_JSON)
							.exchange()
							.expectStatus().isOk()
							.expectBody(Integer.class)
							.consumeWith(entry -> {
								assertEquals(expectedMono, entry.getResponseBody());
							});

	}
	
	@Test
	public void testMonoStream() {
		System.out.println("start test testMonoStream");
		Integer expectedMono = 1;
		webTestClient.get().uri("/functional/monoStream")
							.accept(MediaType.APPLICATION_STREAM_JSON)
							.exchange()
							.expectStatus().isOk()
							.expectBody(Integer.class)
							.consumeWith(entry -> {
								assertEquals(expectedMono, entry.getResponseBody());
							});

	}
}
