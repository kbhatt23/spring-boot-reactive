package com.learning.springreactive;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import reactor.core.publisher.Flux;

public class FluxFactoryMethodsTests extends BaseTest {

	List<String> names = Arrays.asList("sita-ram", "radhe-krishna", "lakshmi-narayan");

	@Test
	public void createFluxFromListBad() {
		//wont be emiting n times on next even
		//whole list will come once
		Flux<List<String>> badApparach = Flux.just(names).log();
		//log will show only one next event and then oncomplete even
		//should have been using mono
		
		badApparach.subscribe(FluxFactoryMethodsTests::findSuccessConsumer,
				FluxFactoryMethodsTests::findErrorConsumer,FluxFactoryMethodsTests::findCompletedConsumer);
		
	}
	
	@Test
	public void createFluxFromListIterable() {
		//will emit n on next events seperately
		Flux<String> badApparach = Flux.fromIterable(names).log();
		
		badApparach.subscribe(FluxFactoryMethodsTests::findSuccessConsumer,
				FluxFactoryMethodsTests::findErrorConsumer,FluxFactoryMethodsTests::findCompletedConsumer);
		
	}
}
