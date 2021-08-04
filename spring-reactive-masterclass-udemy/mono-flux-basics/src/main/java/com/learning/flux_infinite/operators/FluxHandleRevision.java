package com.learning.flux_infinite.operators;

import com.learning.mono_basics.utils.MonoStreamsUtils;

import reactor.core.publisher.Flux;

//handle measn filter + map together
//we get sink object and current element and we can do anything abt it
public class FluxHandleRevision {

	public static void main(String[] args) {
		usingFilterAndMapSeperate();
		
		System.out.println("=================");
		
		usingHandle();
	}

	private static void usingHandle() {
		Flux.range(1, 10)
			.handle((number, sink) -> {
				//filter and map together
				if(number % 2 == 0) // filter
					sink.next(number* 2); // map
			})
			.subscribe(MonoStreamsUtils :: printOnNext,
					MonoStreamsUtils :: printOnError,
					MonoStreamsUtils :: printOnComplete
					);
	}

	private static void usingFilterAndMapSeperate() {
		Flux.range(1, 10)
		    .filter(i -> i % 2 == 0) // only for even
		    .map(i -> i * 2)
			.subscribe(MonoStreamsUtils :: printOnNext,
					MonoStreamsUtils :: printOnError,
					MonoStreamsUtils :: printOnComplete
					);
	}
}
