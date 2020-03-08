package com.learning.springreactive.fluxAndMonoBasics;

import org.junit.Test;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MonoAssertTest {

	@Test
	public void monoBasic() {
		System.out.println("=========started test monoBasic======");
		Mono<String> monString = Mono.just("sita ram").log();
		//Mono<String> monString = Mono.error(() -> new RuntimeException("error me bhee ram"));
		monString
		.subscribe((nextString) -> System.out.println("jai "+nextString)
				, (error) -> System.out.println("error agaya "+error)
				,() -> System.out.println("khel sampanna ho gaya")
				);
	}
	@Test
	public void monoBasicError() {
		System.out.println("=========started test monoBasicError======");
		Mono<String> monString = Mono.error(() -> new RuntimeException("error me bhee ram"));
		monString
		.log()
		.subscribe((nextString) -> System.out.println("jai "+nextString)
				, (error) -> System.out.println("error agaya "+error)
				,() -> System.out.println("khel sampanna ho gaya")
				);
	}
	
	@Test
	public void monoBasicAssert() {
		System.out.println("=========started test monoBasicAssert======");
		Mono<String> monString = Mono.just("sita ram").log();
		StepVerifier.create(monString)
					.expectNext("sita ram")
					.verifyComplete();
	}
	
	@Test
	public void monoBasicErrorAssert() {
		System.out.println("=========started test monoBasicErrorAssert======");
		Mono<String> monString = Mono.error(() -> new RuntimeException("error me bhee ram"));

		StepVerifier.create(monString.log())
		//.expectNext("ka")
					.expectError()
					//after calling verify then only process of test 
					//in subscription starts -> lazy testing
					.verify();
	}
}
