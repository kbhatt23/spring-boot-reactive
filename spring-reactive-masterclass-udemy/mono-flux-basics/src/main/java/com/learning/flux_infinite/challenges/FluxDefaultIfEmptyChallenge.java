package com.learning.flux_infinite.challenges;

import com.learning.mono_basics.utils.MonoStreamsUtils;

import reactor.core.publisher.Flux;

public class FluxDefaultIfEmptyChallenge {

	public static void main(String[] args) {
		Flux.just(1,2,3,4,5)
		    .filter(  i -> i > 4)
		    //.defaultIfEmpty(99)
		    .switchIfEmpty(Flux.just(99,100))
			.subscribe(MonoStreamsUtils :: printOnNext,
					MonoStreamsUtils :: printOnError,
					MonoStreamsUtils :: printOnComplete
					);
	}
}
