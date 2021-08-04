package com.learning.mono_flux_advanced.schedulers;


import com.learning.mono_flux_advanced.utils.ThreadSubscriber;
import com.learning.mono_flux_advanced.utils.ThreadUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

public class SubscribeOnBasic {

	public static void main(String[] args) {
		
		Flux<String> data = Flux.create(SubscribeOnBasic :: createData);
		
		//will be async and non blocking
		//all the task like onnext,onsubcsribe from emitter as well as subscriber will happen in thread pool
		//main will get free
		data
		.subscribeOn(Schedulers.boundedElastic()) // thread count is core * 10 used for blocking i/o calls
		.subscribe(new ThreadSubscriber<>(true, SubscribeOnBasic.class.getSimpleName()));
		
		
		ThreadUtils.sleep(3000);
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
