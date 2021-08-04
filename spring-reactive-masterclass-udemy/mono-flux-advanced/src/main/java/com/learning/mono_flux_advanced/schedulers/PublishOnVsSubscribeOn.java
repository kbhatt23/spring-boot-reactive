package com.learning.mono_flux_advanced.schedulers;

import com.learning.mono_flux_advanced.utils.ThreadSubscriber;
import com.learning.mono_flux_advanced.utils.ThreadUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
//only diff
// if we use publishon it can help use threadpool to do the task but only for subscriber method,
		//in case none of the thread pool is used for publisher then emitter will use main thread
// in subscribe on threadpool is used for both publisher and subscriber method
//with subscribe on we can override the thread pool but only for subscriber part after the method call
import reactor.core.scheduler.Schedulers;

public class PublishOnVsSubscribeOn {

	public static void main(String[] args) {
		Flux.create(PublishOnVsSubscribeOn :: createData)
		.subscribeOn(Schedulers.boundedElastic())
		//however for one subscriber only one thread will be used not more than that
		//only if we subscribe to more than one then one thread is assigned to each of the subscriber tasks
		//as in cold publsiher each subscriber has its won channel
	
		//publishon can only modify subscriber thread pooling do nothing for publisher method
		.publishOn(Schedulers.parallel())
		
		.subscribe(new ThreadSubscriber<>(true, PublishOnVsSubscribeOn.class.getSimpleName()));
	
		ThreadUtils.sleep(5000);
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
