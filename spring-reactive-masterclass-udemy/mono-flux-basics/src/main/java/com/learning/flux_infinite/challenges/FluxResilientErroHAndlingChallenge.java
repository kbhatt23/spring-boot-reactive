package com.learning.flux_infinite.challenges;

import com.learning.mono_basics.utils.MonoStreamsUtils;

import reactor.core.publisher.Flux;

public class FluxResilientErroHAndlingChallenge {

	public static void main(String[] args) {
		
		Flux<Integer> data = Flux.range(1, 4)
			.concatWith(Flux.error(new IllegalStateException("can not generate number")))
			.concatWith(Flux.range(100, 110));
		
		//by default onnext1,2,3,4 and then onError with no reaming messages ignored
		
		data
		//.onErrorReturn(-999) // 1,2,3,4,-999 (onnexts) then complete -> graceful shutdown
		//.onErrorResume(error -> Flux.just(200,201)) // 1,2,3,4,200,201 onnexts and then oncomplete -> gracefull shutdown
		//in both above if error occurs remaing data are ignored completely, however graceful shutdown happens
		
		.onErrorContinue((error,object) -> {}) //we can print the error logs thats it
						//1,2,3,4,100...110 onnexts then complete event
			//basically apart from error other things are processed
			
		.subscribe(MonoStreamsUtils :: printOnNext,
				MonoStreamsUtils :: printOnError,
				MonoStreamsUtils :: printOnComplete
				);	
	}
}
