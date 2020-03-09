package com.learning.springreactive.fluxAndMonoBasics;

import java.time.Duration;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

public class VirtualizedTimeFlux {

	@Test
	public void testinfiniteSeries() throws InterruptedException {
		System.out.println("test started testinfiniteSeries");
		//infinite series
		Flux<Integer> finiteFromInfiniteFluxInteger
		  		= Flux.interval(Duration.ofSeconds(1))
		  			.map(l -> l.intValue())
		  			.log()
		  		;
		
		finiteFromInfiniteFluxInteger.subscribe(ent -> System.out.println("data recieved "+ent));
		Thread.sleep(2000);
		//since time is less all onnext and oncomplete will never come
		//thats benefit of reactive programming
	}
	
	@Test
	public void testinfiniteSeriesWithoutVirtualizedTime() throws InterruptedException {
		System.out.println("test started testinfiniteSeriesWithoutVirtualizedTime");
		//infinite series
		
		Flux<Integer> finiteFromInfiniteFluxInteger
		  		= Flux.interval(Duration.ofSeconds(1))
		  			.map(l -> l.intValue())
		  			.take(4)
		  			.log()
		  		;
		
		//Test stepverifier forces thread to wait
		StepVerifier.create(finiteFromInfiniteFluxInteger)
					.expectSubscription()
					.expectNext(0,1,2,3)
					.verifyComplete();
	}
	
	@Test
	public void testinfiniteSeriesWithVirtualizedTime() throws InterruptedException {
		System.out.println("test started testinfiniteSeriesWithVirtualizedTime");
		//infinite series
		
		//Enabling virtualized time
		VirtualTimeScheduler.getOrSet();
		
		Flux<Integer> finiteFromInfiniteFluxInteger
		  		= Flux.interval(Duration.ofSeconds(1))
		  			.map(l -> l.intValue())
		  			.take(4)
		  			.log()
		  		;
		
		//Test stepverifier forces thread to wait
		//using virtualize type
		//StepVerifier.create(finiteFromInfiniteFluxInteger)
		StepVerifier.withVirtualTime(() -> finiteFromInfiniteFluxInteger)
		.expectSubscription()
		.thenAwait(Duration.ofSeconds(4))
					.expectNext(0,1,2,3)
					.verifyComplete();
	}
}
