package com.learning.mono_flux_advanced.sinks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import com.learning.mono_flux_advanced.utils.DefaultSubscriber;

import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.Many;

//we can share the same sink object among multiple threads publishing the data and multip0le threads recieveing the data and no race condition occurs
//no data loss
public class SinksAreThreadSafe {

	public static void main(String[] args) {
		Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();
		
		sink.asFlux().subscribe(new DefaultSubscriber<>(true,"SinksAreThreadSafe"));
		
		//can not create multiple threads of subscribers even thouggh sink is thread safe as unicast allow single subscriber
		
		Runnable messageTask = () -> {
			IntStream.rangeClosed(1, 100)
					.forEach(i -> sink.tryEmitNext("jai shree ram: "+i+" : "+ Thread.currentThread().getName()));
		};
		
		ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(8);
		newFixedThreadPool.submit(messageTask);
		
		//do not accept any more
		newFixedThreadPool.shutdown();
		
		try {
			newFixedThreadPool.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		System.out.println("main diest");
	}
}
