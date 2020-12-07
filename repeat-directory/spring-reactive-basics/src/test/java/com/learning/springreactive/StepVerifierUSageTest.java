package com.learning.springreactive;

import java.time.Duration;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class StepVerifierUSageTest extends BaseTest{

	@Test
	public void validateSimpleStepVerifier() {
		Flux<String> stringFlux = Flux.just("sita-ram","raja-ram","jai-jai-ram").delayElements(Duration.ofSeconds(1)).log();
		
		StepVerifier.create(stringFlux)
					.expectNextCount(3)
					//.expectNext("sita-ram")
					.verifyComplete();
	}
	@Test
	public void validateSimpleStepVerifier2() {
		Flux<String> stringFlux = Flux.just("sita-ram", "raja-ram", "jai-jai-ram")
				/* .delayElements(Duration.ofSeconds(1)) */.log();
		
		StepVerifier.create(stringFlux)
					//.expectNextCount(3)
					.expectNext("sita-ram")
					.expectNext("raja-ram")
					.expectNext("jai-jai-ram")
					.verifyComplete();
	}
	
	@Test
	public void validateSimpleStepVerifierWithError() {
		Flux<String> stringFlux = Flux.just("sita-ram","raja-ram","jai-jai-ram")
									   .concatWith(Flux.error(RuntimeException::new))
									   //.delayElements(Duration.ofSeconds(1))
									   .log();
		
		StepVerifier.create(stringFlux)
					.expectNext("sita-ram")
					.expectNext("raja-ram")
					.expectNext("jai-jai-ram")
					.expectError()
					//.expectComplete()
					.verify()
					;
	}
	
}
