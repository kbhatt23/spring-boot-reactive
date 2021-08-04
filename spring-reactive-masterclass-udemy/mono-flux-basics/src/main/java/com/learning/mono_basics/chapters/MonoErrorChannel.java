package com.learning.mono_basics.chapters;

import com.learning.mono_basics.utils.MonoStreamsUtils;

import reactor.core.publisher.Mono;

public class MonoErrorChannel {

	public static void main(String[] args) {
		Mono<Integer> data = Mono.just("jai fake baba")
			.map(String::length)
			.map(i -> i / 0) // divided by zero gives runtime exception
			;
		
		//it should directly give one onError event, 0  on next and 0 oncompelte event passed from publisher to subscriber
		
		data.subscribe(
				MonoStreamsUtils :: printOnNext
				,MonoStreamsUtils :: printOnError
				,MonoStreamsUtils :: printOnComplete
				)
		
		;
	}
}
