package com.learning.springreactive.fluxAndMonoBasics;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxFlatMap {
	List<String> namesList = Arrays.asList("ram", "sita" , "lakshman" , "hanuman");
	@Test
	public void flatMapBasic() {
		System.out.println("start test flatMapBasic");
		Flux<String> allFluxEntries = Flux.fromIterable(namesList)
										.map(this::manipulateEntries)//concertin Flux<Stirng> to Flux<List<Strin>>
										.flatMap(items -> Flux.fromIterable(items))
										.log()
										;
		
		StepVerifier.create(allFluxEntries)
					.expectNextCount(8)
					.verifyComplete();
		
	}
	
	@Test
	public void flatMapBasicWithSeperateFluxes() {
		System.out.println("start test flatMapBasicWithSeperateFluxes");
		Flux<List<String>> allFluxEntries = Flux.fromIterable(namesList)
										.map(this::manipulateEntries)//concertin Flux<Stirng> to Flux<List<Strin>>
										//.flatMap(items -> Flux.fromIterable(items))
										.log()
										;
		
		StepVerifier.create(allFluxEntries)
					//.expectNextCount(8)
		//should be 4 as single entry itslef is list
					//also it is not flattened to singly flux
					.expectNextCount(4)
					.verifyComplete();
		
		
	}
	
	
	
		//scenario when input is Flux<Stirng> and output is Flux<List<String>> and we want to convert to Flux<String>
	public List<String> manipulateEntries(String s){
		//Simulation as if DB call or Web service call is going on
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Arrays.asList(s,"raghavbhakt");
	}
}
