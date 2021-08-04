package com.learning.mono_flux_advanced.schedulers;


import com.learning.mono_flux_advanced.utils.ThreadSubscriber;
import com.learning.mono_flux_advanced.utils.ThreadUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

//a subscribeOn can help thread pool to do the task for both emitter and consumer
//but publishon can help only on subscriber task 
//also it can override the behavior of threadpool metnioned by publisher creator
public class SubscribeOnOverride {

	public static void main(String[] args) {
		
		Flux<String> data = Flux.create(SubscribeOnOverride :: createData)
								.subscribeOn(Schedulers.boundedElastic())
				//we are making sure everythign runs ina  differnt thread pool
				//however subscriber can override this but only for consumer part
				//the publisher part of thread eamins intact
				
				;
		
		data
		//publisher part is remained intact at the publisher creator level
		.publishOn(Schedulers.newParallel("overriden"))
		.subscribe(new ThreadSubscriber<>(true, SubscribeOnOverride.class.getSimpleName()));
		
		
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
