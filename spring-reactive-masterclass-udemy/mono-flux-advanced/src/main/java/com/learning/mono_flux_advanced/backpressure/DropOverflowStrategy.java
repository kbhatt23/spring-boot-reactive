package com.learning.mono_flux_advanced.backpressure;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import com.learning.mono_flux_advanced.utils.CountDownSubscriber;
import com.learning.mono_flux_advanced.utils.ThreadUtils;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class DropOverflowStrategy {

	public static void main(String[] args) {
		CountDownLatch  latch = new CountDownLatch(1);
		
		List<Object> dtopppedData = new ArrayList<>();
		
		//for demo lets modify the buffer size
		//so that we can see some data getting lost in drop strategy
		System.setProperty("reactor.bufferSize.small", "100");
		
		Flux.create(sink -> {
			IntStream.rangeClosed(1, 200)
			        //instead of filter we can use takeWhile in jdk9
			         .filter(number -> !sink.isCancelled())
			         .forEach(number -> {
			        	 ThreadUtils.sleep(1);
			        	 sink.next(number);
			         });
			System.out.println(Thread.currentThread().getName()+" completed data creation");
			sink.complete();
		})
		//.onBackpressureDrop() // we will loose some data in buffer, 256 is max buffer size and once it reaches more than that newer elements are dropped
		
		//after buffer full new data will be lost until consumer take out one data
		//however in case of lost data we can use that and pass to another publisher
		.onBackpressureDrop(dtopppedData :: add)
		.publishOn(Schedulers.boundedElastic())
		
		.doOnNext(item -> ThreadUtils.sleep(10))
		.subscribe(new CountDownSubscriber<>(true, "DropOverflowStrategy", latch))
		;
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("main dies");
		
		System.out.println("lost count data "+dtopppedData.size());
	}
}
