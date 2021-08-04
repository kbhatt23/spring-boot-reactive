package com.learning.mono_basics.chapters;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.learning.mono_basics.utils.MonoStreamsUtils;

import reactor.core.publisher.Mono;

public class MonoFromFuture {

	public static void main(String[] args) {
		//in cae of fromFuture the task si asyn and non blocking
		//so if main dies it ignroes data subscribe operation
		Mono<String> fromFuture = Mono.fromFuture(findName());
		
		CountDownLatch latch = new CountDownLatch(1);
		
		//fromfuturesubscribe is asyn and non blocking
		fromFuture.subscribe(MonoStreamsUtils :: printOnNext,
				MonoStreamsUtils :: printOnError,
				() -> {latch.countDown() ;MonoStreamsUtils.printOnComplete();}
				);
		
		try {
			latch.await(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("main dies");
	
	}
	
	public static CompletableFuture<String> findName(){
		//takes 2 seconds in generating name
		return CompletableFuture.supplyAsync(MonoFromSupplier:: findName);
	}
}
