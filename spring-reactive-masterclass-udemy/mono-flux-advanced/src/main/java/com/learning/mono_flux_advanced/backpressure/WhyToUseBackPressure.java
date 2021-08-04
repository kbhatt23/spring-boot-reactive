package com.learning.mono_flux_advanced.backpressure;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import com.learning.mono_flux_advanced.utils.CountDownSubscriber;
import com.learning.mono_flux_advanced.utils.ThreadUtils;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class WhyToUseBackPressure {

	public static void main(String[] args) {
		
		CountDownLatch countDownLatch = new CountDownLatch(1);
		
		Flux.create( sink ->{
			//verry fast as within milisecond it will be done
			//will be kept in memory as consumer is very slow
			IntStream.rangeClosed(1, 500)
			   .forEach(i -> sink.next(i));
			
			System.out.println(Thread.currentThread().getName()+" completed data emitting");
			sink.complete();
		})
		.publishOn(Schedulers.boundedElastic())
		.doOnNext(i -> { ThreadUtils.sleep(10);})
		.subscribe(new CountDownSubscriber<>(true, "WhyToUseBackPressure", countDownLatch))
		
		;
		
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("main dies");
	}
}
