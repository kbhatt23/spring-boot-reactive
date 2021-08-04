package com.learning.flux_basics.chapters;

import com.learning.mono_basics.utils.MonoStreamsUtils;

import reactor.core.publisher.Flux;

public class FluxFromRange {

	public static void main(String[] args) {
		//print name 10 times
		String name = "sita ram";
		
		//starts with number 1
		//go until 10 items are added and for each itration add 1 from previous
		Flux.range(1, 10)
			.map(i -> name)
			.subscribe(MonoStreamsUtils :: printOnNext,
					MonoStreamsUtils :: printOnError,
					MonoStreamsUtils :: printOnComplete
					);
		
		System.out.println("========magic starts===============");
		
		//start fro, 7 and keep on adding oone on eah iteraton. do iteration 11 time
		Flux.range(7, 11)
		.subscribe(MonoStreamsUtils :: printOnNext,
				MonoStreamsUtils :: printOnError,
				MonoStreamsUtils :: printOnComplete
				);
				
	}
}
