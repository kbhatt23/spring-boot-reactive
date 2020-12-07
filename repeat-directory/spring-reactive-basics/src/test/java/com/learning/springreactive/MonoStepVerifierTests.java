package com.learning.springreactive;

import java.time.Duration;

import org.junit.Test;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MonoStepVerifierTests extends BaseTest{

	@Test
	public void basicMono() {
		Mono<String> stringMono = Mono.just("sita-ram").delayElement(Duration.ofSeconds(1));
		
		StepVerifier.create(stringMono)
					.expectNext("sita-ram")
					.verifyComplete();
	}
	
	@Test
	public void basicMonoAllConsumers() {
		Mono<String> stringMono = Mono.empty();
		
		StepVerifier.create(stringMono)
				    .verifyComplete();
	}
	
	@Test
	public void basicMonoError() {
		Mono<String> stringMono = Mono.error(RuntimeException::new);
		
		//empty means directly oncompelte runnable will be called
		//Mono<String> stringMono = Mono.empty();
		
		//if we do not subscribe nothing happens
		StepVerifier.create(stringMono)
					.expectError(RuntimeException.class)
					.verify();		
	}
}
