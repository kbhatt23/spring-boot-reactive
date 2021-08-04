package com.learning.flux_basics.chapters;

import com.learning.mono_basics.utils.MonoStreamsUtils;

import reactor.core.publisher.Flux;

public class FluxWithError {
public static void main(String[] args) {
	
	//direct on error with no onnext and no oncomplete
	//Flux<String> data = Flux.error(() -> new IllegalStateException("can not generate name"));
	
	//this will give onnext till sucess and moment error copmes it give on error event with no oncomplete event
	Flux<String> data = Flux.just("sita ram", "uma mahesh")
		.concatWith(Flux.error(() -> new IllegalStateException("can not generate name")))
		.concatWith(Flux.just("fake name","another fake name"))
		;
	
	//gives 2 onnext message and then one on error 
	// further on next and oncomplete wont be sent
	data.subscribe(MonoStreamsUtils :: printOnNext,
			MonoStreamsUtils :: printOnError,
			MonoStreamsUtils :: printOnComplete
			);
}
}
