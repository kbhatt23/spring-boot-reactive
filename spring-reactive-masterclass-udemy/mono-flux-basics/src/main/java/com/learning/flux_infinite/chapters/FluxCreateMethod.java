package com.learning.flux_infinite.chapters;

import com.learning.mono_basics.utils.DefaultSubscriber;
import com.learning.mono_basics.utils.MonoStreamsUtils;
import com.learning.mono_basics.utils.ThreadUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

public class FluxCreateMethod {

	public static void main(String[] args) {
		
		Flux<String> data = Flux.create(FluxCreateMethod :: emitterCreater);
		
		DefaultSubscriber<String> defaultSubscriber = new DefaultSubscriber<>(true, FluxCreateMethod.class.getSimpleName());
		data.subscribeWith(defaultSubscriber);
	}
	
	public static void emitterCreater(FluxSink<String> sink) {
		
		for(int i =0 ; i < 5 ; i++) {
			//time consuming task
			ThreadUtils.sleep(1000);
			sink.next(MonoStreamsUtils.FAKER.name().firstName());
		}
		sink.complete();
	}
}
