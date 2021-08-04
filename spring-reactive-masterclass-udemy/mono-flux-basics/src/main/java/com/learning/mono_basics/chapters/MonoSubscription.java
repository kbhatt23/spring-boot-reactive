package com.learning.mono_basics.chapters;

import com.learning.mono_basics.utils.MonoStreamsUtils;

import reactor.core.publisher.Mono;

public class MonoSubscription {

	public static void main(String[] args) {
		Mono<Integer> data = Mono.just(1); //-> one onnext even and then one on complete
		//Mono<Integer> data = Mono.empty();  // -> only one oncompelte event
		//Mono<Integer> data = Mono.error(() -> new IllegalStateException("can not generate number"));  // -> only one onerror event is sent
		
		//mono is lazy/cold and hence nothing happens until subscriber subscribes
		//it is async and hence main thread wont wait untill it is done
		//divide the task to another fork join thread and go to next line
		//if this task takes more tuime than main thread dies since forkjoin is daemon thread subscription task is ignored
		data.subscribe(
				MonoStreamsUtils :: printOnNext
				,MonoStreamsUtils :: printOnError
				,MonoStreamsUtils :: printOnComplete
				)
		
		;
	}
}
