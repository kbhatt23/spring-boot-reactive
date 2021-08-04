package com.learning.mono_flux_advanced.schedulers;

import com.learning.mono_flux_advanced.utils.ThreadSubscriber;
import com.learning.mono_flux_advanced.utils.ThreadUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

public class PAralleExecutionFlux {

	public static void main(String[] args) {
		Flux.range(1, 20)
		.parallel()
		.runOn(Schedulers.boundedElastic())
		.subscribe(new ThreadSubscriber<>(false,"PAralleExecutionFlux"))
		;
		
		
		ThreadUtils.sleep(1000);
	}
	
private static void createData(FluxSink<String> sink) {
		
		for(int i = 0 ; i < 2 ; i ++) {
			System.out.println("Thread: "+Thread.currentThread().getName()+ " createData called.");
			if(sink.isCancelled())
				break;
			ThreadUtils.sleep(1000);
			sink.next("jai shree ram "+i);
		}
		
		sink.complete();
	}
}
