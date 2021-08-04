package com.learning.mono_flux_advanced.batching;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.learning.mono_flux_advanced.utils.CountDownSubscriber;
import com.learning.mono_flux_advanced.utils.DefaultSubscriber;
import com.learning.mono_flux_advanced.utils.ThreadUtils;

import reactor.core.publisher.Flux;

public class BufferBatching {

	public static void main(String[] args) {

		//Flux<String> fetchEvents = fetchEvents();
		Flux<String> fetchEvents = fetchLimitedEvents();
		
		CountDownLatch countDownLatch = new CountDownLatch(1);
		fetchEvents
			//lets say we are storing in mongo db
		//we acn use insertMAny
		//meaning we can keep items in buffer until it reaches certain count
		//once that count is reached then only we send one message to subscriber with list of event
		
		//if data is complete then also that many data is passed
		.buffer(4)
		.subscribe(new CountDownSubscriber<>(true, "BufferBatching", countDownLatch));
		
		
		try {
			countDownLatch.await(20, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
		
		System.out.println("main dies");
	}

	private static Flux<String> fetchEvents() {
	return	Flux.interval(Duration.ofSeconds(1))
			.map( i -> "event-"+i);
	}
	
	private static Flux<String> fetchLimitedEvents() {
		return	Flux.range(1, 6)
				.delayElements(Duration.ofSeconds(1))
				.map( i -> "event-"+i);
		}

}
