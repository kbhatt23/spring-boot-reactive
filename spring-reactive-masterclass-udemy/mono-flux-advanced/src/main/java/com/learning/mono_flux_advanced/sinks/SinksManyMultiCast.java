package com.learning.mono_flux_advanced.sinks;

import com.learning.mono_flux_advanced.utils.DefaultSubscriber;
import com.learning.mono_flux_advanced.utils.ThreadUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

//can represent a flux of items
//can allow multiple subscriber as well
public class SinksManyMultiCast {

	public static void main(String[] args) {
		
		//act as a flux for subscriber
		//however only one subscriber can subscribe
		Sinks.Many<String> multicastSink = Sinks.many().multicast().onBackpressureBuffer();
		
		Flux<String> asFlux = multicastSink.asFlux();
		Runnable task = () -> {
			for(int i = 0 ; i < 10 ; i++) {
				ThreadUtils.sleep(1000);
				multicastSink.tryEmitNext("jai shree ram "+i);
			}
			
			multicastSink.tryEmitComplete();
		};
		
		new Thread(task).start();
		
		asFlux.subscribe(new DefaultSubscriber<>(true, "user-1"));
		
		
		ThreadUtils.sleep(4000);
		
		asFlux.subscribe(new DefaultSubscriber<>(true, "user-2"));
		
		
		ThreadUtils.sleep(2000);
		System.out.println("main dies");
	}
}
