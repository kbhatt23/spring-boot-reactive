package com.learning.flux_infinite.operators;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

import com.learning.mono_basics.utils.DefaultSubscriber;
import com.learning.mono_basics.utils.MonoStreamsUtils;

import reactor.core.publisher.Flux;

public class FluxDelay {

	public static void main(String[] args) {
		
		CountDownLatch latch = new CountDownLatch(1);
		
		Flux.range(1, 100)
			.log()
			.delayElements(Duration.ofMillis(100))
			//by default it becomes asyn for delay elements
			.subscribe(MonoStreamsUtils :: printOnNext,
					(error) -> {MonoStreamsUtils.printOnError(error); latch.countDown();},
					() -> {MonoStreamsUtils.printOnComplete(); latch.countDown(); }
					);
	
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
