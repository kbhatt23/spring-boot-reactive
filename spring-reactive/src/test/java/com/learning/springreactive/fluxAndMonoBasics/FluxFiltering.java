package com.learning.springreactive.fluxAndMonoBasics;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxFiltering {
	List<String> namesList = Arrays.asList("ram", "sita" , "lakshman" , "hanuman");
	@Test
	public void filterFluxBasic() {
		System.out.println("start test filterFluxBasic");
		
		Flux<String> allNames= Flux.fromIterable(namesList)
				//should not add below logs as it blocks perfromance
				//.log()
				;
		
		Flux<String> filteredNames = allNames.filter(a -> a.length() <=4 ).log();
		
		StepVerifier.create(filteredNames)
				.expectNext("ram", "sita")
				.expectComplete()
				.verify();
	}
}
