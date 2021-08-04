package com.learning.mono_flux_advanced.backpressure;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import com.learning.mono_flux_advanced.utils.CountDownSubscriber;
import com.learning.mono_flux_advanced.utils.ThreadUtils;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class OnErrorBackpressureStrategy {

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
			        	 System.out.println(Thread.currentThread().getName()+" sending message "+number);
			        	 sink.next(number);
			         });
			System.out.println(Thread.currentThread().getName()+" completed data creation");
			sink.complete();
		})
		.onBackpressureError() // once buffer is full(256 items) and consumer is not able to pull out messages then onError message is recieved by consumer
				//and publisher should use isCancelled method , same like take() method
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
