package com.learning.mono_basics.chapters;

import com.learning.mono_basics.utils.MonoStreamsUtils;
import com.learning.mono_basics.utils.ThreadUtils;

import reactor.core.publisher.Mono;

//same like optional orElse(eager) and orElseGet(lazy)
public class MonoFromSupplier {

	public static void main(String[] args) {
		//just method is eager , even if there is no subscriber it tries to call method and create object
		//it creates a data that will never be pushed to subscriber -> what a loss of memory
		//Mono<String> data = Mono.just(MonoFromSupplier.findName());
		
		//lazy it will call the method only if a subcsriber exist and reject calling data creation method when no subscriber
		Mono<String> data = Mono.fromSupplier(MonoFromSupplier :: findName);
		
//		data
//		.subscribe(MonoStreamsUtils :: printOnNext,
//				MonoStreamsUtils :: printOnError,
//				MonoStreamsUtils :: printOnComplete
//				);
	}
	
	//assume there is a method that takes nothing but gives something
	public static String findName() {
		System.out.println("findName called");
		//time consuming task
		ThreadUtils.sleep(2000);
		
		return "radhe krishna";
	}
}
