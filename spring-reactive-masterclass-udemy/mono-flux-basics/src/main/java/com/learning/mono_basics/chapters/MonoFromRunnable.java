package com.learning.mono_basics.chapters;

import com.learning.mono_basics.utils.MonoStreamsUtils;
import com.learning.mono_basics.utils.ThreadUtils;

import reactor.core.publisher.Mono;

public class MonoFromRunnable {

	public static void main(String[] args) {
		Mono<Object> fromRunnable = Mono.fromRunnable(MonoFromRunnable :: runTask);
		
		//subscribe method is blocking -> only it sis non blocking for fromFuture method as it runs on fork join daemon threads
		fromRunnable.subscribe(MonoStreamsUtils :: printOnNext,
				MonoStreamsUtils :: printOnError,
				() -> {MonoStreamsUtils.printOnComplete();}
				);
	}
	
	//no input , no output , no exception
	public static void runTask() {
		System.out.println("run task started");
		
		ThreadUtils.sleep(2000);
		
		System.out.println("run task completed");
	}
}
