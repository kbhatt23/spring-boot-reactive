package com.learning.springreactive.fluxAndMonoBasics;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAssertTests {
	@Test
	public void assertBasicFluxSuccess() {
		
		System.out.println("============starting test "+"assertBasicFluxSuccess==============");
		Flux<String> fluxStr = Flux.just("ram","sita","hanuman").log();
		
		StepVerifier.create(fluxStr)
		.expectNext("ram","sita")
		//if we comment below it will give asserteion false
		.expectNext("hanuman")
		.expectComplete()
		//if we uncmoment it blows assertion
		//.expectError()
		.verify()
		;
	}
	
	@Test
	public void assertBasicFluxError() {
		
		System.out.println("============starting test "+"assertBasicFluxError==============");
		Flux<String> fluxStr = Flux.just("ram","sita","hanuman")
				.concatWith(Flux.error(RuntimeException::new))
				.log();
		StepVerifier.create(fluxStr)
			.expectNext("ram","sita","hanuman")
			.expectError()
			.verify()
			;
}}
