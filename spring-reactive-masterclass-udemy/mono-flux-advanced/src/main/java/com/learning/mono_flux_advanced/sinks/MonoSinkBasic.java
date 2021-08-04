package com.learning.mono_flux_advanced.sinks;

import com.learning.mono_flux_advanced.utils.DefaultSubscriber;

import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

public class MonoSinkBasic {

	public static void main(String[] args) {

		Sinks.One<String> monoSink = Sinks.one();
		
		
		//can act as both publisher and subscriber
		Mono<String> asMono = monoSink.asMono();
		
		asMono.subscribe(new DefaultSubscriber<>(true, "nono-1"));
		
		asMono.subscribe(new DefaultSubscriber<>(true, "nono-2"));
		
		asMono.subscribe(new DefaultSubscriber<>(true, "nono-3"));
		
		asMono.subscribe(new DefaultSubscriber<>(true, "nono-4"));
		
		//direct onCompelte will be sent as it is mono
		//monoSink.tryEmitEmpty();
		
		//monoSink.tryEmitValue("jai shree ram");
		
		monoSink.tryEmitError(new RuntimeException("can not generate sinkmessage"));
	}
}
