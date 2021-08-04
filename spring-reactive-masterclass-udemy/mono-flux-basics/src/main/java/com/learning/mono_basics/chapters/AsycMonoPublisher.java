package com.learning.mono_basics.chapters;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.learning.mono_basics.utils.MonoStreamsUtils;
import com.learning.mono_basics.utils.ThreadUtils;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class AsycMonoPublisher {

	public static void main(String[] args) {
	
		Mono<String> data = Mono.fromSupplier( AsycMonoPublisher :: findName)
			
		;
		
		CountDownLatch latch = new CountDownLatch(1);
		
		//suscribe emthod is by default blocking
		data
		.subscribeOn(Schedulers.boundedElastic()) // making it async , since it is time consuming main thread dies and hence sbscribe task is ignored
		.subscribe(MonoStreamsUtils :: printOnNext,
				MonoStreamsUtils :: printOnError,
				() -> {latch.countDown() ;MonoStreamsUtils.printOnComplete();}
				);
		
		try {
			latch.await(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("main thread dies");
	}
	
	//assume there is a method that takes nothing but gives something
	public static String findName() {
		System.out.println("findName called");
		//time consuming task
		ThreadUtils.sleep(2000);
		
		return "radhe krishna";
	}
}
