package com.learning.mono_flux_advanced.batching;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.learning.mono_flux_advanced.utils.CountDownSubscriber;
import com.learning.mono_flux_advanced.utils.DefaultSubscriber;
import com.learning.mono_flux_advanced.utils.ThreadUtils;

import reactor.core.publisher.Flux;

public class BufferWithTimeoutBatching {

	public static void main(String[] args) {

		Flux<String> fetchEvents = fetchEvents();
		//Flux<String> fetchEvents = fetchLimitedEvents();
		
		CountDownLatch countDownLatch = new CountDownLatch(1);
		fetchEvents
		//if timeout happens push the so far fetched data
		//if size reaches first then send all 4 data
		.bufferTimeout(4, Duration.ofSeconds(2))
		.subscribe(new CountDownSubscriber<>(true, "BufferBatching", countDownLatch));
		
		
		try {
			countDownLatch.await(20, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
		
		System.out.println("main dies");
	}

	private static Flux<String> fetchEvents() {
	return	Flux.interval(Duration.ofMillis(200))
			.map( i -> "event-"+i);
	}
	
	private static Flux<String> fetchLimitedEvents() {
		return	Flux.range(1, 6)
				.delayElements(Duration.ofSeconds(1))
				.map( i -> "event-"+i);
		}

}
