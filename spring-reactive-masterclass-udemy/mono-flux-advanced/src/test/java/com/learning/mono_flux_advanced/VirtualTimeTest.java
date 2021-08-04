package com.learning.mono_flux_advanced;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class VirtualTimeTest {

	@Test
	public void testTimeout() {
		//lets say it si time consuming
		//and we expect to give limited time
		//if flux is taking more time then expectation give error and test fails
		
		
		Flux<Integer> timeConsumingFlux = Flux.range(1, 5).delayElements(Duration.ofSeconds(1));
		
		StepVerifier.create(timeConsumingFlux)
				.expectSubscription()
				.expectNext(1,2,3,4,5)
				.expectComplete()
				.verify(Duration.ofSeconds(6))
				;
		
	}
	
	@Test
	public void virtualTest1() {
		//lets say it si time consuming
		//and we expect to give limited time
		//we can simulaate the time
		
		
		StepVerifier.withVirtualTime(() -> Flux.range(1, 5).delayElements(Duration.ofSeconds(1)))
				.thenAwait(Duration.ofSeconds(6))
				.expectNext(1,2,3,4,5)
				.expectComplete()
				.verify(Duration.ofSeconds(6))
				;
		
	}
}
