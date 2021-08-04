package com.learning.mono_flux_advanced.sinks;

import com.learning.mono_flux_advanced.utils.DefaultSubscriber;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

//by default multicast stream is hot by share method -> meaning for first subscriber no data loss and for other subscrber old emitted data is lost
//we can make it fully hot using allOrnothing method -> even for first subscriber data can be lost older emitte ones
public class SinksManyHotStream {

	public static void main(String[] args) {
		Sinks.Many<String> multiCastFlux=	Sinks.many().multicast()
				//complete hot stream , evcen for first subscriber data will be lost
				.directAllOrNothing()
				//.onBackpressureBuffer();
		;
		
		Flux<String> asFlux = multiCastFlux.asFlux();
		
	
		multiCastFlux.tryEmitNext("hello");
		
		multiCastFlux.tryEmitNext("how are you");
		
		multiCastFlux.tryEmitNext("?");
		
		//subscribption after data emiitng
		//only first subscriber wont loose data
	asFlux.subscribe(new DefaultSubscriber<>(true, "user-1"));
		
		asFlux.subscribe(new DefaultSubscriber<>(true, "user-2"));
		
		
		//so far subscribed user will recieve
		multiCastFlux.tryEmitNext("jai shree ram");
		
	}
}
