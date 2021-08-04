package com.learning.flux_basics.chapters;

import com.learning.mono_basics.utils.MonoStreamsUtils;

import reactor.core.publisher.Flux;

//always use flux.just when data is prepared already
//this is eager so even though there is no subscriber it still prepares the data for publisher
public class FluxJustCreate {

	public static void main(String[] args) {
		//Flux<Integer> data = Flux.just(1); // -> one onNext and one onComplete event
		
		//subscribe is blocking
		Flux<Integer> data = Flux.just(1,2,3,4); // 4 on next for data and one oncomplete event
		
		//Flux<Integer> data = Flux.empty();  // direct one oncomplete
		data.subscribe(MonoStreamsUtils :: printOnNext,
				MonoStreamsUtils :: printOnError,
				MonoStreamsUtils :: printOnComplete
				);
	}
}
