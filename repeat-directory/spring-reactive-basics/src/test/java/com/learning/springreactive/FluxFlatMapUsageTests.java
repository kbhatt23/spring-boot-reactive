package com.learning.springreactive;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

public class FluxFlatMapUsageTests  extends BaseTest{
	List<String> names = Arrays.asList("sita-ram", "radhe-krishna", "lakshmi-narayan","nothing");
	
	//can return 0,1, or n elements
	public Flux<String> splitItems(String item){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Flux.fromArray(item.split("-"));
	}
	@Test
	public void fluxFlatMapBasic() {
		Flux<String> fromIterable = Flux.fromIterable(names);
		//we split the items from "-"-> that can have 0,1 ,2 or more items and hence flatmap
		//we wanted all words and not just range
		
		//this shud ave individual 6 onnext call and then oncomplete
		Flux<String> flattendFlux = fromIterable
			.flatMap(this::splitItems)
			//.map(String::toUpperCase)
			.log()
			;
		StepVerifier.create(flattendFlux)
				    .expectNext("sita","ram","radhe","krishna")
				    .expectNext("lakshmi","narayan")
				    .expectNext("nothing")
				    .verifyComplete();
	}
	
	
	@Test
	public void fluxFlatMapBasicConsumer() {
		Flux<String> fromIterable = Flux.fromIterable(names);
		//we split the items from "-"-> that can have 0,1 ,2 or more items and hence flatmap
		//we wanted all words and not just range
		
		//this shud ave individual 6 onnext call and then oncomplete
		Flux<String> flattendFlux = fromIterable
			.flatMap(this::splitItems)
			//.map(String::toUpperCase)
			.log()
			;
		
		flattendFlux.subscribe(FluxFlatMapUsageTests::findSuccessConsumer,
				FluxFlatMapUsageTests::findErrorConsumer, FluxFlatMapUsageTests::findCompletedConsumer
				);
	}
	
	
	
	
}
