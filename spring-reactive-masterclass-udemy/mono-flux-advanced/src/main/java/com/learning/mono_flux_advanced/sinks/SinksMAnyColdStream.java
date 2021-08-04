package com.learning.mono_flux_advanced.sinks;

import com.learning.mono_flux_advanced.utils.DefaultSubscriber;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class SinksMAnyColdStream {

	public static void main(String[] args) {
		//cold publisher, even old data will be saved in buffer so that lat subscriber can still get them
		Sinks.Many<String> replayMany=	Sinks.many().replay().all();
		
		Flux<String> asFlux = replayMany.asFlux();
		
	
		replayMany.tryEmitNext("hello");
		
		replayMany.tryEmitNext("how are you");
		
		replayMany.tryEmitNext("?");
		
	asFlux.subscribe(new DefaultSubscriber<>(true, "user-1"));
		
		asFlux.subscribe(new DefaultSubscriber<>(true, "user-2"));
		
		
		//so far subscribed user will recieve
		replayMany.tryEmitNext("jai shree ram");
		
	
	}
}
