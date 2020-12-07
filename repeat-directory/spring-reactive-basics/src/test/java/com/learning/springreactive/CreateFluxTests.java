package com.learning.springreactive;

import java.time.Duration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import reactor.core.publisher.Flux;

//no need to load spring boot whole context -> saves time 
//no need to load mongi, netty,embededmongo etc
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class CreateFluxTests extends BaseTest{
	
	@Test
	public void createBasic() {
		Flux<String> stringFlux = Flux.just("one", "two",
				"three")/* .log() *//* .delayElements(Duration.ofSeconds(1)) */;
		stringFlux.subscribe(CreateFluxTests::findSuccessConsumer);
	}
	
	@Test
	public void createBasicWithError() {
		Flux<String> stringFlux = Flux.just("one","two","three");
		//Flux<String> stringFlux = Flux.error(() -> new RuntimeException("Exception occurred while finding string Flux"));
		
		//another way to create error
		//concat 2 flux together -> this way we can first see succes consumers for 3 elements and then suddenly error
		boolean throwError = false;
		if(throwError) {
			stringFlux = stringFlux.concatWith(Flux.error(() -> new RuntimeException("Exception occurred while finding string Flux"))).log();
		}else {
			stringFlux=stringFlux.log();
		}
		
		
		stringFlux.subscribe(CreateFluxTests::findSuccessConsumer,
				CreateFluxTests::findErrorConsumer,
				CreateFluxTests::findCompletedConsumer);
		
	}
	
	@Test
	public void createBasicWithErrorAndThenAddMoreItems() {
		Flux<String> stringFlux = Flux.just("one","two","three");
		//Flux<String> stringFlux = Flux.error(() -> new RuntimeException("Exception occurred while finding string Flux"));
		
		//another way to create error
		//concat 2 flux together -> this way we can first see succes consumers for 3 elements and then suddenly error
		boolean throwError = false;
		Flux<String> appendMore = Flux.just("four","five");
		if(throwError) {
			stringFlux = stringFlux.concatWith(Flux.error(() -> new RuntimeException("Exception occurred while finding string Flux"))).
					concatWith(appendMore)
					.log();
		}else {
			stringFlux = stringFlux.concatWith(appendMore).log();
		}
		stringFlux.subscribe(CreateFluxTests::findSuccessConsumer,
				CreateFluxTests::findErrorConsumer,CreateFluxTests::findCompletedConsumer);
		
	}
}
