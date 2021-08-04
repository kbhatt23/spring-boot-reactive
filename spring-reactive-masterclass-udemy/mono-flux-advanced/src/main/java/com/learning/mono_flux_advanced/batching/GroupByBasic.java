package com.learning.mono_flux_advanced.batching;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;

public class GroupByBasic {

	public static void main(String[] args) {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		Flux.range(1, 20)
		    .delayElements(Duration.ofSeconds(2))
		    //.log()
		    .groupBy(number -> number % 2 == 0 ? "even": "odd" )
		    //.log()
		    .subscribe(GroupByBasic :: mangeGropedFlux
		    		,err -> System.out.println("main: Exception occurred "+err)
		    		,() -> {System.out.println("main: All task completed"); countDownLatch.countDown();}
		    		)
		    ;
		
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
	}
		System.out.println("main dies");
	}

	private static void mangeGropedFlux(GroupedFlux<String, Integer> groupedFlux) {
		
		groupedFlux
		 .doOnComplete(() -> System.out.println("mangeGropedFlux: tasks completed"))
		.subscribe(message -> System.out.println("mangeGropedFlux: Message recieved with key "+groupedFlux.key()+" and value "+message))
		;
	}
}
