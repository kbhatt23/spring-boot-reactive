package com.learning.flux_infinite.challenges;

import com.learning.mono_basics.utils.MonoStreamsUtils;
import com.learning.mono_basics.utils.ThreadUtils;

import reactor.core.publisher.Flux;

//limit rate is likea  buffer
//if subscriber asks for all items middle man buffer only asks for 100 items
//100 items are stored in buffer and consumer keep on taking one by one using onnext
//once 75 items are finished from buffer, limit rate buffer again asks for another 75 data from publisher
//this process continues
public class LimitRateChallenge {

	public static void main(String[] args) {
		//called once so we need to loop to add onnext methods
		//oncancel is mandaotry even using take emthod or canccelled on subscription by subscriber
//		Flux<String> dataStream = Flux.create(sink ->{
//			for(int  i = 0 ; i < 1000 ; i ++) {
//				if(sink.isCancelled()) {
//					break;
//				}
//				sink.next(MonoStreamsUtils.FAKER.name().fullName());
//			}
//			sink.complete();
//		});
		
		Flux<String> dataStream = Flux.range(1, 1000).map(String::valueOf);
		
		dataStream
		.log()
		.limitRate(100,90) // get data of 100 initially let subscriber take it one by one and once 25 items are reaming call another 75 items
		.subscribe(MonoStreamsUtils :: printOnNext,
				MonoStreamsUtils :: printOnError,
				MonoStreamsUtils :: printOnComplete
				);		
	}
}
