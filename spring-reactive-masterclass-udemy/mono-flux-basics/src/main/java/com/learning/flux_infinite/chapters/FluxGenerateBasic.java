package com.learning.flux_infinite.chapters;

import com.learning.mono_basics.utils.DefaultSubscriber;
import com.learning.mono_basics.utils.MonoStreamsUtils;
import com.learning.mono_basics.utils.ThreadUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

//generate method is called recursively automatically
public class FluxGenerateBasic {

	public static void main(String[] args) {
		Flux.generate(FluxGenerateBasic :: synchronousSink
				)
		.take(4) // we can force first 5 manually , auto cancel happens to publisher
			.subscribeWith(new DefaultSubscriber<>(true, FluxGenerateBasic.class.getSimpleName()))
			;
	}
	
	private static void synchronousSink(SynchronousSink<String> synchronousSink) {
		System.out.println("synchronousSink called");
		
		//time consuming task
		ThreadUtils.sleep(1000);
		synchronousSink.next(MonoStreamsUtils.FAKER.country().name());
	}
}
