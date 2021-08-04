package com.learning.flux_infinite.challenges;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

import com.learning.mono_basics.utils.MonoStreamsUtils;

import reactor.core.publisher.Flux;

//delay elements make use of limitrate
//buffer sits in between pub and sub
//buffer takes 32 items from pub and store there and pushes to sub based on delay time
public class DelayElementsChallenge {

	public static void main(String[] args) {
		//Flux<String> infiniteNames = Flux.generate(sink -> sink.next(MonoStreamsUtils.FAKER.name().fullName()));
		
		Flux<String> infiniteNames = Flux.range(1, 100).map(String::valueOf);
		CountDownLatch latch = new CountDownLatch(1);
		infiniteNames
		.log()
		.delayElements(Duration.ofSeconds(1))  // uses limit rate buffer asks for 32 items and keep it ,
											// all data recieved by buffer at once bu it pushes data to subscriber based on delay time
		.subscribe(MonoStreamsUtils :: printOnNext,
				MonoStreamsUtils :: printOnError,
				() -> {MonoStreamsUtils.printOnComplete(); latch.countDown();}
				);	
		
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
