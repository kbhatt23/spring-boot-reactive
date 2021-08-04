package com.learning.mono_flux_advanced.schedulers;

import com.learning.mono_flux_advanced.utils.ThreadSubscriber;
import com.learning.mono_flux_advanced.utils.ThreadUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

public class MultiplePublishSubscribeOns {

	public static void main(String[] args) {
		Flux<String> data = Flux.create(MultiplePublishSubscribeOns :: createData);
		
		data
		.map(i -> {System.out.println(Thread.currentThread().getName() +" : pre transformation "); return i;})
			.publishOn(Schedulers.newParallel("waah1"))
			.map(i -> {System.out.println(Thread.currentThread().getName() +" : first transformation "); return i;})
			.publishOn(Schedulers.newParallel("waah2"))
			.map(i -> {System.out.println(Thread.currentThread().getName() +" : second transformation "); return i;})
			
			.subscribeOn(Schedulers.boundedElastic())
			
			//remember only first subscriberon amtter as data emitter can only be know by flux creator dev
			//so consumer can use subscribe on only if dev has not mentioned but if metnioned can not override thread for emitter
			.subscribeOn(Schedulers.boundedElastic())
			
			.subscribeOn(Schedulers.newParallel("fake"))
			.subscribe(new ThreadSubscriber<>(true, MultiplePublishSubscribeOns.class.getSimpleName()))
			;
		
		ThreadUtils.sleep(3000);
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


