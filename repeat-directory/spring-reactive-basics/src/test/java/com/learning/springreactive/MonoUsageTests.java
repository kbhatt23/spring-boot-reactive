package com.learning.springreactive;

import org.junit.Test;

import reactor.core.publisher.Mono;

public class MonoUsageTests extends BaseTest{

	@Test
	public void basicMono() {
		Mono<String> stringMono = Mono.just("sita-ram");
		
		//if we do not subscribe nothing happens
		stringMono.subscribe(System.out::println);
		
	}
	
	@Test
	public void basicMonoAllConsumers() {
		Mono<String> stringMono = Mono.just("sita-ram").log();
		
		//empty means directly oncompelte runnable will be called
		//Mono<String> stringMono = Mono.empty();
		
		//if we do not subscribe nothing happens
		stringMono.subscribe(MonoUsageTests::findSuccessConsumer,
				MonoUsageTests::findErrorConsumer,MonoUsageTests::findCompletedConsumer
				);
		
		
	}
	
	@Test
	public void basicMonoError() {
		Mono<String> stringMono = Mono.error(() -> new RuntimeException("error in mono"));
		
		//empty means directly oncompelte runnable will be called
		//Mono<String> stringMono = Mono.empty();
		
		//if we do not subscribe nothing happens
		stringMono.subscribe(MonoUsageTests::findSuccessConsumer,
				MonoUsageTests::findErrorConsumer,MonoUsageTests::findCompletedConsumer
				);
		
	}
}
