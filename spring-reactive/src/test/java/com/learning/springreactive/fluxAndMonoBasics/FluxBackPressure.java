package com.learning.springreactive.fluxAndMonoBasics;

import org.junit.Test;

import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxBackPressure {
	
	@Test
	public void testBackPressure() {
		System.out.println("start test testBackPressure");
		Flux<Integer> finiteFlux = Flux.range(1, 8).log();
		
		StepVerifier.create(finiteFlux)
					.expectSubscription()
					//custom request entry
					.thenRequest(1)
					.expectNext(1)
					.thenRequest(2)
					.expectNext(2,3)
					//forcefully canceling as we need ot test first thre elenmtns only
					.thenCancel()
					//after cacle neither oncomplete or onerror gets called
					//without cacnel it wont work as stream will contiue
					//and test will fail
					.verify();
	}
	
	@Test
	public void iterationWithoutBAckPressure() {
		System.out.println("starting test iterationWithoutBAckPressure");
		//in infinite case thread(subscription method) becomes non blcking
		//in finite stream it becomes blocking
		//creating data in form of flux publisher
		Flux<Integer> finiteFlux = Flux.range(1, 8).log();
		
		
		//as part of back pressure we want only 4 elements
		finiteFlux.subscribe(//onnext result
				(entry) -> System.out.println("entry fetched "+entry)
				, //onError case
				(exception) -> System.err.println("Exception occurred "+exception)
				, //onComplete event
				() -> System.out.println("All Task complete")
				//, //subscription case
				//requesting only 4 elements
				//if we do not give 4th method argument it is unbounded
				//meaning it will go till 8 and then oncomplete
				//(subscription) -> subscription.request(4)
				)
		;
	}

	@Test
	public void basicBackPressure() {
		System.out.println("starting test basicBackPressure");
		//in infinite case thread(subscription method) becomes non blcking
		//in finite stream it becomes blocking
		//creating data in form of flux publisher
		Flux<Integer> finiteFlux = Flux.range(1, 8).log();
		
		//as part of back pressure we want only 4 elements
		finiteFlux.subscribe(//onnext result
				(entry) -> System.out.println("entry fetched "+entry)
				, //onError case
				(exception) -> System.err.println("Exception occurred "+exception)
				, //onComplete event
				() -> System.out.println("All Task complete")
				, //subscription case
				//requesting only 4 elements
				//if we do not give 4th method argument it is unbounded
				//meaning it will go till 8 and then oncomplete
				//if we do nto call cancel then publkisher sends messages to subscriber
				//however subscriber do not process it
				(subscription) -> subscription.request(4)
				)
		;
	}
	
	@Test
	public void basicBackPressureWithCancel() {
		//with cancel enhances the performance as the load from publisher will get reduced
		System.out.println("starting test basicBackPressureWithCancel");
		//in infinite case thread(subscription method) becomes non blcking
		//in finite stream it becomes blocking
		//creating data in form of flux publisher
		Flux<Integer> finiteFlux = Flux.range(1, 8).log();
		
		//as part of back pressure we want only 4 elements
		finiteFlux.subscribe(//onnext result
				(entry) -> System.out.println("entry fetched "+entry)
				, //onError case
				(exception) -> System.err.println("Exception occurred "+exception)
				, //onComplete event
				() -> System.out.println("All Task complete")
				, //subscription case
				//requesting only 4 elements
				//if we do not give 4th method argument it is unbounded
				//meaning it will go till 8 and then oncomplete
				(subscription) -> {subscription.request(4);
				//cancel event stope further event sending from publisher
				//this includes onError or onComplete
					subscription.cancel();
				}
				)
		;
	}
	@Test
	public void manageCustomBackPRessure() {
		System.out.println("starting test manageCustomBackPRessure");
		Flux<Integer> finiteFlux = Flux.range(1, 8).log();
		
		finiteFlux.subscribe(
				//we can create seperate class also
				new BaseSubscriber<Integer>() {
					//gets called on every onNext event
					protected void hookOnNext(Integer value) {
						//calling request function for 1 entries only each time
						//after 4 request we can cancel it
						request(1);
						if(value == 4) {
							cancel();
						}
					};
					//hooking after all complete
					protected void hookOnComplete() {
						//this wont come
						System.out.println("All taks completed");
						
					};
					
					protected void hookOnCancel() {
						System.out.println("All taks cacnelled");
						
					};
				}
				
				);
		
	}
	
	@Test
	public void manageCustomSubscriber() {
		System.out.println("starting test manageCustomSubscriber");
		Flux<Integer> finiteFlux = Flux.range(1, 4)
				.concatWith(Flux.error(new CustomException("error me bhee ram")))
				.concatWith(Flux.just(9,10))
				.log();
		
		finiteFlux.subscribe(new CustomSubscriber());
	
	}
	
}
