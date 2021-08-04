package com.learning.flux_infinite.challenges;

import com.learning.mono_basics.utils.MonoStreamsUtils;
import com.learning.mono_basics.utils.ThreadUtils;

import reactor.core.publisher.Flux;

public class FluxHandleChallenge {

	public static void main(String[] args) {
		//infinite stream as genrate consumer is called eveytime with no manual oncomplete or onerror
		Flux<String> inifiniteNames = Flux.generate(synchronousSink -> {
			ThreadUtils.sleep(1000);
			synchronousSink.next(MonoStreamsUtils.FAKER.name().fullName());
			})
		;
		
		inifiniteNames.handle((fullName , sink) -> {
			sink.next(fullName);
			
			if(fullName.toLowerCase().startsWith("c"))
				sink.complete();
		})
		.subscribe(MonoStreamsUtils :: printOnNext,
				MonoStreamsUtils :: printOnError,
				MonoStreamsUtils :: printOnComplete
				);
		;
	}
}
