package com.learning.springreactive.fluxAndMonoBasics;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@SpringBootTest
public class FluxMonoBasicTest {
@Test
public void basicFlux() {
	System.out.println("=================starting test "+"basicFlux=======================");
	Flux<String> flux = //adding data to data source reprsentted as publisher
			Flux.just("ram" , "sita" , "lakshman" , "hanuman")
			//logs all the events between publisher and subscriber
			.log();
	System.out.println("===============");
	//to add subscriber to publisher added above
	//first param-> conumer that takes after call of on
	flux.subscribe(System.out::println
			//to handle the exception -? called when onError event is triggerd
			,System.err::println
			//called when onComplete even is triggerred
			,() -> System.out.println("jai raghav ki"));
}

@Test
public void basicFluxWithEsception() {
	System.out.println("=================starting test "+"basicFluxWithEsception================");
	Flux<String> flux = Flux.just("ram" , "sita" , "lakshman" , "hanuman")
			//to add exception with flux
			.concatWith(Flux.error(() -> new RuntimeException("hey ram bachao exception se")))
			//adding more scnerarios ot test if error breaks processing of future events
			.concatWith(Flux.just("after error ka data"))
			.log()
			;
	System.out.println("===============");
	flux.subscribe(System.out::println
			//to handle onError event
			,System.err::println
			//to handle onSucess event
			,System.out::println);
}


}
