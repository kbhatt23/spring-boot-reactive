package com.learning.flux_infinite.operators;

import java.time.Duration;

import com.learning.mono_basics.utils.DefaultSubscriber;
import com.learning.mono_basics.utils.ThreadUtils;

import reactor.core.publisher.Flux;

public class FluxTimeout {

	public static void main(String[] args) {
		Flux<Integer> orders = Flux.range(1, 10)
		    .delayElements(Duration.ofSeconds(5)) // verry slow process
		    ;
		
		//timeoutWithNoFallback(orders);
		
		timeoutWithFallback(orders);
		
		ThreadUtils.sleep(20000);
	}

	private static void timeoutWithFallback(Flux<Integer> orders) {
		orders.timeout(Duration.ofSeconds(2),fallback())
	     .subscribeWith(new DefaultSubscriber<>(true, "timeoutWithFallback"));
		
	}
	
	private static Flux<Integer> fallback(){
		return Flux.range(100, 110).delayElements(Duration.ofSeconds(1));
	}

	private static void timeoutWithNoFallback(Flux<Integer> orders) {

		orders.timeout(Duration.ofSeconds(2))
		     .onErrorReturn(-10) // after timeout returns this and ignores remaining data
		     .subscribeWith(new DefaultSubscriber<>(true, "timeoutWithNoFallback"));
	}
}
