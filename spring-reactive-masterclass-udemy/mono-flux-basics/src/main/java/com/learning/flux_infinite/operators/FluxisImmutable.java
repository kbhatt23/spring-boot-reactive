package com.learning.flux_infinite.operators;

import com.learning.mono_basics.utils.MonoStreamsUtils;

import reactor.core.publisher.Flux;

public class FluxisImmutable {

	public static void main(String[] args) {
		Flux<Integer> numbers = Flux.range(1, 10);
		
		//every oeprator method makes a new flux object only
		numbers.map(i -> i * 2);
		
		//here we update the variable to this new object
		//numbers  = numbers.map(i -> i * 2);
		
		numbers.subscribe(MonoStreamsUtils :: printOnNext,
				MonoStreamsUtils :: printOnError ,
				MonoStreamsUtils :: printOnComplete);
		   
	}
}
