package com.learning.springreactive;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxFactoryMethodsStepVerifierTests extends BaseTest {

	List<String> names = Arrays.asList("sita-ram", "radhe-krishna", "lakshmi-narayan");

	@Test
	public void createFluxFromListBad() {
		//wont be emiting n times on next even
		//whole list will come once
		Flux<List<String>> badApparach = Flux.just(names).log();
		//log will show only one next event and then oncomplete even
		//should have been using mono
		//below will fail as index values are jumbled
		//List<String> expected = Arrays.asList( "radhe-krishna","sita-ram", "lakshmi-narayan");
		
		List<String> expected = Arrays.asList("sita-ram", "radhe-krishna", "lakshmi-narayan");
		
		StepVerifier.create(badApparach)
				//will expect list.equals to be true, meaning size shud be same and each index shud have equals of element to true
					.expectNext(expected)
					.verifyComplete();
		
	}
	
	@Test
	public void createFluxFromListIterable() {
		//will emit n on next events seperately
		Flux<String> goodApproach = Flux.fromIterable(names).log();
		
		StepVerifier.create(goodApproach)
		//elements comming one by one on next 3 times
				    .expectNext("sita-ram", "radhe-krishna", "lakshmi-narayan")
				    .verifyComplete();
	}
	
	@Test
	public void createFluxFromArray() {
		//will emit n on next events seperately
		Flux<String> goodApproach = Flux.fromArray(names.toArray(new String[names.size()]));
		
		StepVerifier.create(goodApproach)
		//elements comming one by one on next 3 times
				    .expectNext("sita-ram", "radhe-krishna", "lakshmi-narayan")
				    .verifyComplete();
	}
	
	@Test
	public void createFluxFromStream() {
		//will emit n on next events seperately
		//yusing supplier heps in lazy loading
		Flux<String> goodApproach = Flux.fromStream(names::stream);
		
		StepVerifier.create(goodApproach)
		//elements comming one by one on next 3 times
				    .expectNext("sita-ram", "radhe-krishna", "lakshmi-narayan")
				    .verifyComplete();
	}
	
	@Test
	public void monFactory() {
		//returns mono.empty if null other wise that mon0
		Mono<String> monoItem = Mono.justOrEmpty(null);
		//empty means direct complete
		StepVerifier.create(monoItem)
					 .verifyComplete();
		
		 monoItem = Mono.justOrEmpty("sita-ram");
		//empty means direct complete
		StepVerifier.create(monoItem)
					 .expectNext("sita-ram")
					 .verifyComplete();
		
		
	}
	
	@Test
	public void monFactorySupplier() {
		//returns mono.empty if null other wise that mono
		//examplet aken from badapprach , single item will come of type list
		Mono<List<String>> monoItem = Mono.fromSupplier(() -> names).log();
		
		StepVerifier.create(monoItem)
				    .expectNext(names)
				    .verifyComplete()
				    ;
				    
		
	}
	
	@Test
	public void fluxRange() {
		Flux<Integer> rangeFlux = Flux.range(1, 11).log();
		
		StepVerifier.create(rangeFlux)
					.expectNextCount(11)
					.verifyComplete();
	}
	
	@Test
	public void fluxInfinite() {
		Flux<Integer> infiniteFlux = Flux.fromStream(Stream.iterate(1, i -> i+1).limit(11)).log()
								
				;		
		StepVerifier.create(infiniteFlux)
		.expectNextCount(11)
		.verifyComplete();
	}
}
