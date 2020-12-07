package com.learning.springreactive;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxMonoTransfoirmationTests {
	List<String> names = Arrays.asList("sita-ram", "radhe-krishna", "lakshmi-narayan");
	@Test
	public void testfluxTransformation() {
		
		Flux<String> transformedFlux = Flux.fromIterable(names).map(String::toUpperCase).log();
		
		StepVerifier.create(transformedFlux)
					.expectNext("SITA-RAM")
					.expectNext("RADHE-KRISHNA")
					.expectNext("LAKSHMI-NARAYAN")
					.verifyComplete();
	}
	
	@Test
	public void testfluxTransformationLength() {
		
		Flux<Integer> transformedFlux = Flux.fromIterable(names).map(String::length).log();
		
		StepVerifier.create(transformedFlux)
					.expectNext(8,13,15)
					.verifyComplete();
	}
	
	@Test
	public void testfluxTransformationLengthRepeat() {
		
		Flux<Integer> transformedFlux = Flux.fromIterable(names).map(String::length)
				.repeat(2)//only in case of success it repeats the whole flux
				.retry(2)//only in case of error it reepats
				.log();
		
		StepVerifier.create(transformedFlux)
					.expectNext(8,13,15,8,13,15,8,13,15)
					.verifyComplete();
	}
	
	@Test
	public void testfluxTransformationWithFilter() {
		
		Flux<String> transformedFlux = Flux.fromIterable(names)
											.filter(i -> i.contains("ram"))
											.map(String::toUpperCase).log().delayElements(Duration.ofSeconds(1));
		
		StepVerifier.create(transformedFlux)
					.expectNext("SITA-RAM")
					//.expectNext("RADHE-KRISHNA")
					//.expectNext("LAKSHMI-NARAYAN")
					.verifyComplete();
	}
}
