package com.learning.mono_flux_advanced.schedulers;


import com.learning.mono_flux_advanced.utils.ThreadSubscriber;
import com.learning.mono_flux_advanced.utils.ThreadUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

public class DefaultThreadBehavior {

	public static void main(String[] args) {
		
		Flux<String> data = Flux.create(DefaultThreadBehavior :: createData);
		
		//apart form interval and delaylemetns method others are blocking by default
		//whatever thread subscribes will do all the task
		data.subscribe(new ThreadSubscriber(true, DefaultThreadBehavior.class.getSimpleName()));
		
		System.out.println(Thread.currentThread().getName()+" dies.");
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
