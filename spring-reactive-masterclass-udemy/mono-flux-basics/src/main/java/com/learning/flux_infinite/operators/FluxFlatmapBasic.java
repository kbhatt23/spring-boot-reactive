package com.learning.flux_infinite.operators;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

import com.learning.mono_basics.utils.MonoStreamsUtils;

import reactor.core.publisher.Flux;

public class FluxFlatmapBasic {

	public static void main(String[] args) {
		CountDownLatch latch = new CountDownLatch(1);
		
		Flux<Integer> data = Flux.range(1, 10)
		    .delayElements(Duration.ofSeconds(1));
		
		
		data.flatMap(FluxFlatmapBasic :: doubleValue)
		.subscribe(MonoStreamsUtils :: printOnNext,
				MonoStreamsUtils :: printOnError,
				() -> {MonoStreamsUtils.printOnComplete(); latch.countDown();}
				);	
		
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("main dies");
	}
	
	public static Flux<Integer> doubleValue(int number){
		return Flux.just(number * 2);
	}
}
