package com.learning.mono_flux_advanced.batching;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.learning.mono_flux_advanced.utils.CountDownSubscriber;
import com.learning.mono_flux_advanced.utils.MonoStreamsUtils;

import reactor.core.publisher.Flux;

public class SelfRepeatLearning2 {

	public static void main(String[] args) {
		
		CountDownLatch countDownLatch = new CountDownLatch(1);
		
		Flux<String> create = Flux.create(fluxSink -> {
			if(!fluxSink.isCancelled()) {
				fluxSink.next(MonoStreamsUtils.FAKER.funnyName().name());
			}
			fluxSink.complete(); });
		
		create
		.repeat(11) // repeat based on count repeat from begining the same data 11 times
		
		.subscribe(new CountDownSubscriber<>(true, "SelfRepeatLearning", countDownLatch));
		
		try {
			countDownLatch.await(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("main dies");
	}
}
