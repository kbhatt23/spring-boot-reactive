package com.learning.flux_infinite.operators;

import com.learning.mono_basics.utils.MonoStreamsUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FluxErrorHanding {

	public static void main(String[] args) {
		Flux.range(1, 10)
		    .log()
		    .map( i -> (i / (i-5)))
		    //.onErrorReturn(-1) // in case of error return this value and ignore remaining data
		    
		   // .onErrorResume(FluxErrorHanding :: fallback) // in case of error return fallback and ignore reaming data
		    
		    //below means ignore the error and process reaming data
		    .onErrorContinue((error, object) ->{

		    })
		    .subscribe(
		    		MonoStreamsUtils :: printOnNext
		    		,MonoStreamsUtils :: printOnError
		    		, MonoStreamsUtils :: printOnComplete
		    		)
		
		    ;
		
		
	}
	private static Mono<Integer> fallback(Throwable e) {
		return Mono.just(-11);
	}
}
