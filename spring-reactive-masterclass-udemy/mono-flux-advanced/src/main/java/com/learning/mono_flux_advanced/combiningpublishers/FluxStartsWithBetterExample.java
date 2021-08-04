package com.learning.mono_flux_advanced.combiningpublishers;

import java.util.ArrayList;
import java.util.List;

import com.learning.mono_flux_advanced.utils.DefaultSubscriber;
import com.learning.mono_flux_advanced.utils.MonoStreamsUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

public class FluxStartsWithBetterExample {
	private static final List<String> NAMES ;
	
	static {
		NAMES = new ArrayList<>();
		//sita ram always exist
		NAMES.add("sita ram");
		
	}

	public static void main(String[] args) {
		Flux<String> infiniteNameGenerator = Flux.generate(FluxStartsWithBetterExample :: generateName);
		
		//cache is already preapred hence using fromIterable
		Flux<String> cacheName = Flux.fromIterable(NAMES);
		
		//always take first from cache and if consumer still needs more take from infinite stream
		
		infiniteNameGenerator.startWith(cacheName)
					.take(4)
					.subscribe(new DefaultSubscriber<>(true, "FluxStartsWithBetterExample"));
		
		System.out.println("==============");
		//cache alreayd have it so no need to regenrate it
		infiniteNameGenerator.startWith(cacheName)
		.take(5)
		.subscribe(new DefaultSubscriber<>(true, "FluxStartsWithBetterExample-2"));
		
		//already cahced
		infiniteNameGenerator.startWith(cacheName)
		.filter(i -> i.toLowerCase().startsWith("a"))
		.take(2)
		.subscribe(new DefaultSubscriber<>(true, "FluxStartsWithBetterExample-3"));
	}
	
	
	//generate infinite stream of names
	//however we only want that if subscriner needs for more
	private static void generateName(SynchronousSink<String> fluxSink) {
		System.out.println(Thread.currentThread().getName()+" starts generating name");
		
		String fullName = MonoStreamsUtils.FAKER.name().fullName();
		NAMES.add(fullName);
		fluxSink.next(fullName);
	}
}
