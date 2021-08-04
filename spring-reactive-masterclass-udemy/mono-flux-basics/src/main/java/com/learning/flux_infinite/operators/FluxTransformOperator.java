package com.learning.flux_infinite.operators;

import com.learning.mono_basics.utils.MonoStreamsUtils;

import reactor.core.publisher.Flux;

public class FluxTransformOperator {

	public static void main(String[] args) {
		Flux.range(1, 10)
		    .transform(FluxTransformOperator :: transormDouble)
		    .subscribe(MonoStreamsUtils :: printOnNext,
					MonoStreamsUtils :: printOnError,
					MonoStreamsUtils :: printOnComplete
					);	
	}
	
	private static Flux<String> transormDouble(Flux<Integer> inputFlux){
	
		return inputFlux.filter(i -> i % 2 ==0)
				    .map(i -> i * 2)
				    .map(String::valueOf)
				    ;
	}
}
