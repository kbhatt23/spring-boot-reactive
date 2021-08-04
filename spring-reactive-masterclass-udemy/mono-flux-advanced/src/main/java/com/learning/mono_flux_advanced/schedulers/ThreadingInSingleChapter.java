package com.learning.mono_flux_advanced.schedulers;

import java.util.stream.IntStream;

import com.learning.mono_flux_advanced.utils.ThreadSubscriber;
import com.learning.mono_flux_advanced.utils.ThreadUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

//here i am demonstration all concepts at once

//flux create is lazy until some one subscribes it do not call the emitter method consumer
// by default subscribe method is done using main and hence all task like publish data emitting and consumer onnext,oncomplet etc method are done using thread main
//by main means who ever is calling subscribe method
//using asyn scheulder do not enforce data paralleism
// for one subscriber only one thread will do the whole task of onnext,oncomplet, publisher data emitting etc
//if 2 subscribers are there one thread each from pool will do all the tasks for their individual things

public class ThreadingInSingleChapter {

	public static void main(String[] args) {
		Flux<String> data = Flux.create(ThreadingInSingleChapter :: createEmitter);
		
		//Runnable r = () ->
		data
		.subscribeOn(Schedulers.boundedElastic())
		.map(i -> {System.out.println(Thread.currentThread().getName()+" pre transformation"); ; return i;})
		//.subscribeOn(Schedulers.parallel())
		.publishOn(Schedulers.parallel()) // overrides only for consumer
		//if we call it n times the latest thread model will be used for pipeline method but not for emitter
		
		.map(i -> {System.out.println(Thread.currentThread().getName()+" first transformation"); ; return i;})
		
		.publishOn(Schedulers.boundedElastic())
		.map(i -> {System.out.println(Thread.currentThread().getName()+" second transformation"); ; return i;})
		.subscribeOn(Schedulers.parallel())
		.subscribeOn(Schedulers.single())
		.subscribe(new ThreadSubscriber<>(true, "ThreadingInSingleChapter"));
		
		//IntStream.rangeClosed(1, 2).forEach(number -> new Thread(r).start());
		
//		data
//		.subscribeOn(Schedulers.boundedElastic())
//		.subscribe(new ThreadSubscriber<>(true, "ThreadingInSingleChapter-2"));
		
		ThreadUtils.sleep(4500);
		System.out.println(Thread.currentThread().getName()+" dies.");
	}
	
	//flux create is lazy until some one subscribes it do not call the emitter method consumer
	private static void createEmitter(FluxSink<String> fluxSink) {
		
		for(int i = 0 ; i < 4 ; i ++) {
			ThreadUtils.sleep(1000);
			System.out.println(Thread.currentThread().getName()+ " calling method createEmitter");
			fluxSink.next("jai shree ram "+i);
		}
		
		fluxSink.complete();
	}
}
