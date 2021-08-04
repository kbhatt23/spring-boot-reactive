package com.learning.flux_infinite.chapters;

import com.learning.mono_basics.utils.DefaultSubscriber;
import com.learning.mono_basics.utils.MonoStreamsUtils;
import com.learning.mono_basics.utils.ThreadUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

//generate method is called recursively automatically
//after any country that start with c should stop emitting
public class FluxGenerateBasicConditional {

	public static void main(String[] args) {
		Flux.generate(FluxGenerateBasicConditional :: synchronousSink
				)
			.subscribeWith(new DefaultSubscriber<>(true, FluxGenerateBasicConditional.class.getSimpleName()))
			;
		
	}
	
	private static void synchronousSink(SynchronousSink<String> synchronousSink) {
		System.out.println("synchronousSink called");
		
		//time consuming task
		ThreadUtils.sleep(1000);
		String country = MonoStreamsUtils.FAKER.country().name();
		synchronousSink.next(country);
		if(country.toLowerCase().startsWith("c"))
			synchronousSink.complete();
	}
}
