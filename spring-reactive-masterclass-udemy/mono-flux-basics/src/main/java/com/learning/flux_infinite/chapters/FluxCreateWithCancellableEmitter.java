package com.learning.flux_infinite.chapters;

import com.learning.mono_basics.utils.DefaultSubscriber;
import com.learning.mono_basics.utils.MonoStreamsUtils;
import com.learning.mono_basics.utils.ThreadUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

public class FluxCreateWithCancellableEmitter {

	public static void main(String[] args) {
		
		Flux.create(FluxCreateWithCancellableEmitter :: fluxSink)
		   .take(4) // here in emitter case take can not cancel the publisher to keep emitting data -> waste of power
		   			//een ater complete is done for subscriber, thread waits as publisher keeps emitting data
		   .log()
		   .subscribeWith(new DefaultSubscriber<>(true, FluxCreateWithCancellableEmitter.class.getSimpleName()));
	}
	
	private static void fluxSink(FluxSink<String> fluxSink) {
		
		for(int i = 0 ; i < 10 ; i++) {
			//in case subscriber cancels it using take method or manually cancels publisher should stop emitting data
			if(fluxSink.isCancelled()) {
				break;
			}
			
			ThreadUtils.sleep(1000);
			fluxSink.next(MonoStreamsUtils.FAKER.name().fullName());
		}
		
		fluxSink.complete();
	}
	
}
