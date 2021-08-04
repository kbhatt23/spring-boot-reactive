package com.learning.mono_flux_advanced.batching;

import java.time.Duration;
import java.util.function.Function;

import com.learning.mono_flux_advanced.utils.DefaultSubscriber;
import com.learning.mono_flux_advanced.utils.ThreadUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class WindowByBasic {

	private static final String JAI_SHREE_RAM = "jai shree ram";

	public static void main(String[] args) {
		Flux<String> events = Flux.interval(Duration.ofSeconds(1))
		    .map(i -> "event-"+i);
		
		
		events
		//.window(5)
		.window(Duration.ofSeconds(4))
			 // .flatMap(Function.identity())
		//.map(WindowByBasic :: manipulateWindow)
		.map(WindowByBasic :: manipulateWindow2)
		.flatMap(Function.identity())
		.subscribe(new DefaultSubscriber<>(true, WindowByBasic.class.getSimpleName()))
			  ;
		
		ThreadUtils.sleep(20000);
		
		System.out.println("main dies");
	}
	
	private static Mono<String> manipulateWindow(Flux<String> dataFlux){
		Runnable innerTask = () -> System.out.println("succesfully processed batch.");
		Runnable task = () -> {
			dataFlux.doOnComplete( innerTask);
		};
		return Mono.fromRunnable(task );
	}
	
	private static Mono<String> manipulateWindow2(Flux<String> dataFlux){
		
		return dataFlux.doOnNext(event -> System.out.println("manipulateWindow2: processing event "+event))
				.doOnComplete(() -> System.out.println("manipulateWindow2: sucessfully processed batch"))
				.then(Mono.just(JAI_SHREE_RAM));
		
	}
}
