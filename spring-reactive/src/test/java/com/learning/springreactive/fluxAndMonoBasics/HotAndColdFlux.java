package com.learning.springreactive.fluxAndMonoBasics;

import java.time.Duration;

import org.junit.Test;

import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

public class HotAndColdFlux {

	//@Test
	public void coldPublisher() throws InterruptedException {
		System.out.println("========test started coldPublisher========");
		Flux<String> coldFlux = Flux.just("A" , "B" , "C" , "D" , "E" , "F")
						//after one data emission it waits for 1 second
						//and then only data is pushed
								.delayElements(Duration.ofSeconds(1))
								.log();
				;
				//subscribe is non blocking
		coldFlux.subscribe((entry) -> System.out.println("Subscriber 1 data recieved "+entry));
		//if we sleep for 2 seconds we simply ignore others and thread contiues
		//this is how non blockin propgramming work
		//this is reactive programming, reciever do not wait until forecefully waited
		//however Stepverifier test class cn force to wait so that test can be performed
		Thread.sleep(3000);
		//in cold flus it starts from first entry
		coldFlux.subscribe((entry) -> System.out.println("Subscriber 2 data recieved "+entry));
 	
			//without sleep it wont wait and we will loose data
			//as ther eis already a delay in pubkisher side
		Thread.sleep(4000);
		//in cold flux it starts from begining and if sleep is less than total time than
		//we loose data at the end
	}
	
	@Test
	public void hotPublisher() throws InterruptedException {
		System.out.println("========test started hotPublisher========");
		Flux<String> coldFlux = Flux.just("A" , "B" , "C" , "D" , "E" , "F")
						//after one data emission it waits for 1 second
						//and then only data is pushed
								.delayElements(Duration.ofSeconds(1))
								.log();
	ConnectableFlux<String> hotFlux = coldFlux.publish();
	hotFlux.connect();

	hotFlux.subscribe((entry) -> System.out.println("Subscriber 1 data recieved "+entry));
	Thread.sleep(3000);
	
	hotFlux.subscribe((entry) -> System.out.println("Subscriber 2 data recieved "+entry));
	
	Thread.sleep(4000);
	}
}
