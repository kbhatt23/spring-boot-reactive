package com.learning.springreactive;

import java.time.Duration;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class CombineFluxAndMonoTests extends BaseTest {
	Flux<String> first = Flux.just("sita-ram", "radhe-krisna").delayElements(Duration.ofSeconds(1));
	Flux<String> second = Flux.just("lakshmi-narayan", "lakshmi-narasimha").delayElements(Duration.ofSeconds(1));

	@Test
	public void concatFluxes() {
		
		
		Flux<String> combined = first.concatWith(second).log();

		// StepVerifier.create(combined).expectNextCount(4).verifyComplete();

		StepVerifier.create(combined).expectNext("sita-ram", "radhe-krisna", "lakshmi-narayan", "lakshmi-narasimha")
				.verifyComplete();
	}

	@Test
	public void mergeFluxes() {
		// merge means combining elements
		// hoiwever the concumer do not recieve elements in exact order but it comes on
		// basis of what comes first
		Flux<String> combined = Flux.merge(second, first).log();
		// in merge we can not say 100 percent hte seqiunce of elemnet, whatever comes
		// first is sent to subscriber first
		StepVerifier.create(combined).expectNextCount(4).verifyComplete();
	}

	@Test
	public void zipUsage() {
		// one by one get elements at same index, like for firs titeration get 0th
		// element of first flux and 0th of second and then so on
		Flux<String> zip = Flux.zip(first, second, (a, b) -> a.concat("+").concat(b)).log();

		StepVerifier.create(zip).expectNext("sita-ram+lakshmi-narayan", "radhe-krisna+lakshmi-narasimha")
				.verifyComplete();

	}
}
