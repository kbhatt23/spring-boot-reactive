package com.learning.mono_flux_advanced.schedulers;


import com.learning.mono_flux_advanced.utils.ThreadSubscriber;
import com.learning.mono_flux_advanced.utils.ThreadUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

public class DefaultThreadBehaviorWithMultipleThread {

	public static void main(String[] args) {
		
		Flux<String> data = Flux.create(DefaultThreadBehaviorWithMultipleThread :: createData);
		
		//apart form interval and delaylemetns method others are blocking by default
		Runnable task = () -> data.subscribe(new ThreadSubscriber<>(true, DefaultThreadBehaviorWithMultipleThread.class.getSimpleName()));
		
		//remember the current running thread will actually do all the task like onnext oncomplete etc
		//here we have 2 threads doing so one each is doing
		
		for(int i = 0 ;   i < 2 ; i ++) {
			new Thread(task).start();
		}
		
		
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
