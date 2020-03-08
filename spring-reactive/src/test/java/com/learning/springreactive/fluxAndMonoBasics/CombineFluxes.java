package com.learning.springreactive.fluxAndMonoBasics;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
public class CombineFluxes {
	private List<String> entriesListA = Arrays.asList("a","b","c");
	private List<String> entriesListB = Arrays.asList("d","e","f");
	
	@Test
	public void combineUsingMerge() {
		System.out.println("========start test combineUsingMerge=========");
		Flux<String> fluxA = Flux.fromIterable(entriesListA);
		Flux<String> fluxB = Flux.fromIterable(entriesListB);
		//sequence do not matter
		//paralele threads run synchronously
		Flux<String> combined = Flux.merge(fluxA,fluxB).log();
		
		StepVerifier.create(combined)
		.expectNext("a","b","c","d","e","f")
					.verifyComplete();
	}
	
	@Test
	public void combineUsingMergeWithDelay() {
		System.out.println("========start test combineUsingMergeWithDelay=========");
		Flux<String> fluxA = Flux.fromIterable(entriesListA).delayElements(Duration.ofSeconds(1));
		Flux<String> fluxB = Flux.fromIterable(entriesListB).delayElements(Duration.ofSeconds(1));
		//sequence do not matter
		//paralele threads run synchronously
		Flux<String> combined = Flux.merge(fluxA,fluxB).log();
		
		StepVerifier.create(combined)
					.expectNext("a","b","c","d","e","f")
					//.expectNextCount(6)
					.verifyComplete();
	}
	
	@Test
	public void combineUsingConcat() {
		System.out.println("========start test combineUsingConcat=========");
		Flux<String> fluxA = Flux.fromIterable(entriesListA).delayElements(Duration.ofSeconds(1));
		Flux<String> fluxB = Flux.fromIterable(entriesListB).delayElements(Duration.ofSeconds(1));
		//sequence will matter
		//paralele threads run synchronously
		Flux<String> combined = Flux.concat(fluxA,fluxB);
		
		StepVerifier.create(combined.log())
					.expectNext("a","b","c","d","e","f")
					//.expectNextCount(6)
					.verifyComplete();
	}
	
	@Test
	public void combineUsingZip() {
		System.out.println("========start test combineUsingZip=========");
		Flux<String> fluxA = Flux.fromIterable(entriesListA).delayElements(Duration.ofSeconds(1));
		Flux<String> fluxB = Flux.fromIterable(entriesListB).delayElements(Duration.ofSeconds(1));
		//zip combined the fluxes entries at nth place and put together
		//here a and b represent nth elecment of the individualy fluxes A and B respectively
		Flux<String> combined = Flux.zip(fluxA,fluxB, (a,b) -> a.concat(b));
		
		StepVerifier.create(combined.log())
					.expectNext("ad","be","cf")
					//.expectNextCount(6)
					.verifyComplete();
	}

}
