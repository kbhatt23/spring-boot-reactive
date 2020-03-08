package com.learning.springreactive.fluxAndMonoBasics;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxFactoryMethods {

	String[] names = new String[] {"ram" , "sita" , "lakshman" , "hanuman"};
	@Test
	public void fluxFromIterable() {
		System.out.println("=====started fluxFromIterable========");
		List<String> namesList = new ArrayList<String>();
		namesList.add("ram");
		namesList.add("sita");
		
		Flux<String> namesFlux = Flux.fromIterable(namesList).log();
		
		StepVerifier.create(namesFlux)
				.expectNext("ram","sita")
				.verifyComplete();
		
	}
	
	@Test
	public void fluxFromArray() {
		System.out.println("=====started fluxFromArray========");
		
		Flux<String> namesFlux = Flux.fromArray(names).log();
		
		StepVerifier.create(namesFlux)
				.expectNext("ram","sita")
				.expectNext("lakshman", "hanuman")
				.verifyComplete();
		
	}
	
	@Test
	public void fluxFromStream() {
		System.out.println("=====started fluxFromStream========");
		List<String> namesList = new ArrayList<String>();
		namesList.add("ram");
		namesList.add("sita");
		Flux<String> namesFlux = Flux.fromStream(namesList.stream()).log();
		
		
		StepVerifier.create(namesFlux)
				.expectNext("ram","sita")
				//.expectNext("lakshman", "hanuman")
				.verifyComplete();
		
	}
	
}
