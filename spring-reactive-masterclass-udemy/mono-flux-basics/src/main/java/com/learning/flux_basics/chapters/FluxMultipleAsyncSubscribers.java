package com.learning.flux_basics.chapters;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class FluxMultipleAsyncSubscribers {

	public static void main(String[] args) {
		//subscribe method is blocking
		//so untill it all cokpletes main thread is blocked and hence can not execute other subscriber
		
		//had to add delay to demonstrate that subscriber are running in parallel
		Flux<Integer> data = Flux.just(1,2,3,4).delayElements(Duration.ofSeconds(1));
		
		CountDownLatch latch = new CountDownLatch(2);
		
		data
		.subscribeOn(Schedulers.boundedElastic()) // will be given to asyn thread and main is free
		.subscribe((message) -> System.out.println("subscriber 1:  recieves "+message)
				, error -> System.out.println("subscriber 1: error occurred "+error)
				, () -> { latch.countDown(); System.out.println("subscriber 1: All task completed");}
				);
		
		
		//subscriber 2 -> can not start as main is blocked until subscriber1 is finished
		data
		.filter(number -> number % 2 == 0) // print only even numbers
		.subscribeOn(Schedulers.boundedElastic()) // will be given to asyn thread and main is free
		.subscribe((message) -> System.out.println("subscriber 2:  recieves "+message)
				, error -> System.out.println("subscriber 2: error occurred "+error)
				, () -> { latch.countDown(); System.out.println("subscriber 2: All task completed");}
				);
		
		
		try {
			latch.await(100, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("FluxMultipleBlockingSubscribers.main dies");
	}
}
