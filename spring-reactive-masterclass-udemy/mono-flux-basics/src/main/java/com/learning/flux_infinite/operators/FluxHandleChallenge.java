package com.learning.flux_infinite.operators;

import java.time.Duration;

import com.learning.mono_basics.utils.DefaultSubscriber;
import com.learning.mono_basics.utils.MonoStreamsUtils;
import com.learning.mono_basics.utils.ThreadUtils;

import reactor.core.publisher.Flux;

public class FluxHandleChallenge {

	
	public static void main(String[] args) {
		
		//generate infinite stream
		Flux<String> generate = Flux.generate(synchronousSink -> synchronousSink.next(MonoStreamsUtils.FAKER.name().fullName()));
			
		generate
		.map(String :: toUpperCase)
		.handle((fullName,synchronousSink) -> {
			ThreadUtils.sleep(1000);
			synchronousSink.next(fullName);
			
			if(fullName.startsWith("C"))
				synchronousSink.complete();
		}).subscribeWith(new DefaultSubscriber<>(true, FluxHandleChallenge.class.getSimpleName()))
			;
		
		
	}
}
