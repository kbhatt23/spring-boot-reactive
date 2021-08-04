package com.learning.flux_basics.chapters;

import reactor.core.publisher.Flux;

public class FluxMultipleBlockingSubscribers {

	public static void main(String[] args) {
		//subscribe method is blocking
		//so untill it all cokpletes main thread is blocked and hence can not execute other subscriber
		
		Flux<Integer> data = Flux.just(1,2,3,4);
		
		//subscriber 1 -> blocking so main will not go to next line untill it is completed
		data.subscribe((message) -> System.out.println("subscriber 1:  recieves "+message)
				, error -> System.out.println("subscriber 1: error occurred "+error)
				, () -> System.out.println("subscriber 1: All task completed")
				);
		
		
		//subscriber 2 -> can not start as main is blocked until subscriber1 is finished
		data
		.filter(number -> number % 2 == 0) // print only even numbers
		.subscribe((message) -> System.out.println("subscriber 2:  recieves "+message)
				, error -> System.out.println("subscriber 2: error occurred "+error)
				, () -> System.out.println("subscriber 2: All task completed")
				);
		
		System.out.println("FluxMultipleBlockingSubscribers.main dies");
	}
}
