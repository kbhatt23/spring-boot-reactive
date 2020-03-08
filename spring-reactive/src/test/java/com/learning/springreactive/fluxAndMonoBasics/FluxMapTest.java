package com.learning.springreactive.fluxAndMonoBasics;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxMapTest {
	List<String> namesList = Arrays.asList("ram", "sita" , "lakshman" , "hanuman");
	@Test
	public void convertToUpperCase() {
		System.out.println("start test convertToUpperCase");
		
		Flux<String> namesListUpperCase = Flux.fromIterable(namesList)
			.map(String::toUpperCase)
			.log();
		
		StepVerifier.create(namesListUpperCase)
					.expectNext("RAM", "SITA" , "LAKSHMAN" , "HANUMAN")
					.expectComplete()
					.verify();
	}
	
	@Test
	public void convertToLength() {
		System.out.println("start test convertToLength");
		
		Flux<Integer> namesListUpperCase = Flux.fromIterable(namesList)
			.map(String::length)
			.log();
		
		StepVerifier.create(namesListUpperCase)
					.expectNext(3,4,8,7)
					.expectComplete()
					.verify();
	}
	
	@Test
	public void convertToLengthWithFilter() {
		System.out.println("start test convertToLengthWithFilter");
		
		Flux<Integer> namesListUpperCase = Flux.fromIterable(namesList)
				.filter(name -> name.length()<5)
			.map(String::length)
			.log();
		
		StepVerifier.create(namesListUpperCase)
					.expectNext(3,4)
					.expectComplete()
					.verify();
	}
}
