package com.learning.springreactive.fluxAndMonoBasics;

import org.junit.Test;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MonoFactoryMethods {

	@Test
	public void monoNullable() {
		System.out.println("=====started monoNullable========");
		//this will give null pointer just like Optional.of can not take null
		
		StepVerifier.create(Mono.just(null).log())
			.verifyComplete();
	}
	
	@Test
	public void monoValidNullable() {
		System.out.println("=====started monoValidNullable========");
		StepVerifier.create(Mono.justOrEmpty(null).log())
			.verifyComplete();
	}
	
	@Test
	public void monoSupplier() {
		System.out.println("=====started monoSupplier========");
		StepVerifier.create(Mono.fromSupplier(() -> "messi").log())
			.expectNext("messi")
			.verifyComplete();
	}
}
