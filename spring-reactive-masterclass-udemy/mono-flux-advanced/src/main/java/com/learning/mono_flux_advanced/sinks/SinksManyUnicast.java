package com.learning.mono_flux_advanced.sinks;

import com.learning.mono_flux_advanced.utils.DefaultSubscriber;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

//can represent a flux of items
//however only one subscriber can subscribe
public class SinksManyUnicast {

	public static void main(String[] args) {
		
		//act as a flux for subscriber
		//however only one subscriber can subscribe
		Sinks.Many<String> unicastSink = Sinks.many().unicast().onBackpressureBuffer();
		
		Flux<String> asFlux = unicastSink.asFlux();
		
		asFlux.subscribe(new DefaultSubscriber<>(true, "user-1"));
		
		asFlux.subscribe(new DefaultSubscriber<>(true, "user-2"));
		
		unicastSink.tryEmitNext("jai shree ram");
		
		unicastSink.tryEmitNext("jai uma mahesh");
		
		unicastSink.tryEmitComplete();
	}
}
