package com.learning.mono_flux_advanced.schedulers;


import java.util.concurrent.Executors;

import com.learning.mono_flux_advanced.utils.ThreadSubscriber;
import com.learning.mono_flux_advanced.utils.ThreadUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

//different thread pools are useful for different oepration
//closes one will do that specific job
//as each method/oepration in pipeline is nothing but subscriber and each can have differnt thread model
public class MultiplSubscribeOn {

	public static void main(String[] args) {
		
		Flux<String> data = Flux.create(MultiplSubscribeOn :: createData);
		
		data
		//onnext oncomplete etc of subscriber will be taken care by paralle thread
		.map(String :: toUpperCase) // this will be done by paralle thread
		.subscribeOn(Schedulers.boundedElastic())
		.subscribe(new ThreadSubscriber<>(true, "subscriber-1"));
		
		
		//second subscriber
		//remeber each of independent subscriber gets one thread only to execute theri things
		//one thread for each subscriber so one single thread will still be doing all task of one subscriber
		data
		//onnext oncomplete etc of subscriber will be taken care by paralle thread
		.map(String :: toUpperCase) // this will be done by paralle thread
		//.subscribeOn(Schedulers.parallel())
		.subscribeOn(Schedulers.boundedElastic())
		.subscribe(new ThreadSubscriber<>(true, "subscriber-2"));
		
		ThreadUtils.sleep(5200);
		System.out.println(Thread.currentThread().getName()+" dies.");
	}
	
	private static void createData(FluxSink<String> sink) {
		
		for(int i = 0 ; i < 5 ; i ++) {
			System.out.println("Thread: "+Thread.currentThread().getName()+ " createData called.");
			if(sink.isCancelled())
				break;
			ThreadUtils.sleep(1000);
			sink.next("jai shree ram "+i);
		}
		
		sink.complete();
	}
}
