package com.learning.springreactive.fluxAndMonoBasics;

import java.time.Duration;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxInfiniteReactiveStream {
@Test
public void createInnfiniteSeries() throws InterruptedException {
	System.out.println("===========started test createInnfiniteSeries==========");
	//interval ,method helps in infinite series of data every duration mentioned as input
	Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(200))
			//.take(5)//making it finite -> 0,1,2,3,4
			.log();
	//this will not get printed as it is completely non blocking
	//subscribe function is always non blocking
	infiniteFlux.subscribe(num -> System.out.println("got value "+num));
	
	//we can make main mehtod to sleep to see the data in inifinite series stream
	
	Thread.sleep(3000);
}

@Test
public void createInnfiniteSeriesAndTest() throws InterruptedException {
	System.out.println("===========started test createInnfiniteSeriesAndTest==========");
	//interval ,method helps in infinite series of data every duration mentioned as input
	Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(200))
			.take(5)//making it finite -> 0,1,2,3,4
			//without take we wont be able to test this as it will keep on going
			.log();
	
	//Step Verifier blocks the flow as testing is needed 
	StepVerifier.create(infiniteFlux)
				.expectSubscription()
				.expectNext(0L,1L,2L,3L,4L)
				.verifyComplete();
	
}
@Test
public void convertInfiniteSeriesAndTest() {
	System.out.println("===========started test createInnfiniteSeriesAndTest==========");
	Flux<Integer> integerFluxLimit = Flux.interval(Duration.ofMillis(100))
			.take(4)//limiting 4 entries out infinite otherwise we cna not test
			.map(l -> l.intValue());
	
	StepVerifier.create(integerFluxLimit)
				.expectSubscription()
				.expectNext(0,1,2,3)
				.verifyComplete();
	
}
}
