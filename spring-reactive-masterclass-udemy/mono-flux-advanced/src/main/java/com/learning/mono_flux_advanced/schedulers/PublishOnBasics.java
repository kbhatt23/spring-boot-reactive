package com.learning.mono_flux_advanced.schedulers;


import com.learning.mono_flux_advanced.utils.ThreadSubscriber;
import com.learning.mono_flux_advanced.utils.ThreadUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

//a subscribeOn can help thread pool to do the task for both emitter and consumer
//but publishon can help only on subscriber task 
//also it can override the behavior of threadpool metnioned by publisher creator
public class PublishOnBasics {

	public static void main(String[] args) {
		
		Flux<String> data = Flux.create(PublishOnBasics :: createData);
		
		data
		.publishOn(Schedulers.boundedElastic()) //publish on can help deriving a new thread but only for subscriber part
		//publisher part is remained intact at the publisher creator level
		.subscribe(new ThreadSubscriber<>(true, PublishOnBasics.class.getSimpleName()));
		
		
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
