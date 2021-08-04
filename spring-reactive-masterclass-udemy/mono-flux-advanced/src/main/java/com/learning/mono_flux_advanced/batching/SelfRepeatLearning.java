package com.learning.mono_flux_advanced.batching;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

import com.learning.mono_flux_advanced.utils.CountDownSubscriber;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class SelfRepeatLearning {

	public static void main(String[] args) {
		
		CountDownLatch countDownLatch = new CountDownLatch(1);
		
		Flux<Integer> dataFlux = Flux.range(1, 4)
				.delayElements(Duration.ofSeconds(1))
				.concatWith(Mono.error(() -> new RuntimeException("can not generate number")))
				.concatWith(Flux.range(5, 3).delayElements(Duration.ofSeconds(1)))
				.repeat(2)
				;
		
		dataFlux.subscribe(new CountDownSubscriber<>(true, "SelfRepeatLearning", countDownLatch));
		
		
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("main dies");
	}
}
