package com.learning.flux_infinite.operators;

import com.learning.mono_basics.utils.DefaultSubscriber;

import reactor.core.publisher.Flux;

public class FluxDefaultIFEmpty {

	public static void main(String[] args) {
		//useDefaultIFEmpty();
		
		useSwitchIfEmpty();
	}

	private static void useSwitchIfEmpty() {

		Flux.range(1, 10)
		    .filter( i -> i > 10) // forces no data to come -> means bvy default no onnext message but single oncompleete event
			.switchIfEmpty(Flux.just(100,101))
		    .subscribeWith(new DefaultSubscriber<>(true, "FluxDefaultIFEmpty"))
		;
	
	}

	private static void useDefaultIFEmpty() {
		Flux.range(1, 10)
		    .filter( i -> i > 10) // forces no data to come -> means bvy default no onnext message but single oncompleete event
			
		    .defaultIfEmpty(-100) // in case none of the data came to subscriber lets push one onnext with data -100 and then oncompelte
		    .subscribeWith(new DefaultSubscriber<>(true, "FluxDefaultIFEmpty"))
		;
	}
}
