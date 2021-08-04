package com.learning.flux_infinite.challenges;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

import com.learning.mono_basics.utils.MonoStreamsUtils;

import reactor.core.publisher.Flux;

//we call external webservice first
//if time out happens then it calls D.B, but if no timeout happens it takes data from webservice
public class FluxTimeoutChallenge {

	public static void main(String[] args) {
		CountDownLatch latch = new CountDownLatch(1);
		Flux<String> webServiceCall = webServiceCall();
		
		webServiceCall.timeout(Duration.ofSeconds(2),dbCall()) // 2 seconds timeout exception
													  //if data does not come within time it give onError by default
		.subscribe(MonoStreamsUtils :: printOnNext,
				(error) -> {MonoStreamsUtils.printOnError(error); latch.countDown();},
				() -> {MonoStreamsUtils.printOnComplete(); latch.countDown(); }
				);
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static Flux<String> webServiceCall(){
		return Flux.interval(Duration.ofSeconds(3)) // each data comes after 5 seconds
			.map(i -> "webServiceCall: "+MonoStreamsUtils.FAKER.name().fullName())
			.take(10)
			;
			
	}
	
	public static Flux<String> dbCall(){
		return Flux.interval(Duration.ofSeconds(2)) // each data comes after 5 seconds
			.map(i -> "dbCall: "+MonoStreamsUtils.FAKER.name().fullName())
			.take(10)
			;
			
	}
}
