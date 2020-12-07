package com.learning.springreactive;

import java.time.Duration;
import java.util.stream.IntStream;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class InfiniteStreamsTests extends BaseTest{

	@Test
	public void simpleLongInfinite() {
		Flux<Long> infiniteLongs = Flux.interval(Duration.ofSeconds(2))
			//.take(11)
				.take(3)
			.log()
			;
		
		StepVerifier.create(infiniteLongs)
				.expectSubscription()
				.expectNextCount(3)
				.verifyComplete();
		
	}
	
	@Test
	public void simpleLongInfinite_2() {
		Flux<Long> infiniteLongs = Flux.interval(Duration.ofSeconds(1))
				//.take(11)
					//.take(3)
				.log()
				;
			
			StepVerifier.create(infiniteLongs)
					.expectSubscription()
					.expectNext(0L,1L,2L,3L,4L,5L,6L,7L)
					.thenCancel()
					.verify();
	
	}
}
