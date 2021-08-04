package com.learning.mono_flux_advanced;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class BasicFluxTest {

	@Test
	public void basicFlux() {
		Flux<Integer> flux = Flux.just(1,2,3,4);
		
		//stepverifier is blocking so even timer thing it can wait and main test thread waits
		StepVerifier.create(flux)
			.expectSubscription()
			.expectNext(1,2)
			.expectNext(3,4)
			.expectComplete()
			.verify()
		;
	}
	
	@Test
	public void basicFluxdelayed() {
		Flux<Integer> flux = Flux.just(1,2,3,4).delayElements(Duration.ofSeconds(1));
		
		//stepverifier is blocking so even timer thing it can wait and main test thread waits
		StepVerifier.create(flux)
			.expectSubscription()
			.expectNext(1,2)
			.expectNext(3,4)
			.expectComplete()
			.verify()
		;
	}
	
	@Test
	public void basicErrorFlux() {
		Flux<Integer> flux = Flux.just(1,2).concatWith(Flux.error(() -> new IllegalStateException("can not generate number")));
		
		StepVerifier.create(flux)
		   .expectSubscription()
		   .expectNext(1,2)
		   .expectErrorMatches(error -> error.getClass().equals(IllegalStateException.class) && error.getMessage().equals("can not generate number"))
		   .verify();
	}
	
	@Test
	public void testEven()
	{
		Flux<Integer> evenFlux = Flux.range(1, 1000)
		   .filter(i -> i % 2 == 0) 
		   ;
		
		StepVerifier.create(evenFlux)
				.expectSubscription()
				//.assertNext(n -> assertTrue(n % 2 == 0))
				//.expectNextMatches(n -> n % 2 == 0)
				.expectNextCount(500)
				.verifyComplete();
	}
}
