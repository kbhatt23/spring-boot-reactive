package com.learning.mono_flux_advanced.combiningpublishers;

import java.time.Duration;

import com.learning.mono_flux_advanced.utils.DefaultSubscriber;
import com.learning.mono_flux_advanced.utils.ThreadUtils;

import reactor.core.publisher.Flux;

public class SelfMerge {

	public static void main(String[] args) {
		//Flux<Integer> fluxA = Flux.range(1, 5);
		
		Flux<String> fluxA = Flux.interval(Duration.ofSeconds(1)).take(4).map(n -> "fluxA- "+n);
		
		//Flux<Integer> fluxB = Flux.range(10, 5);
		
		Flux<String> fluxB = Flux.interval(Duration.ofSeconds(1))
				.take(4)
				.map(n -> "fluxB- "+n);
		
		//will be opposite to startsWith
		//lets eat fluxA first and if still consumer wants more take it from fluxB
		
		Flux.merge(fluxA,fluxB)
		.subscribe(new DefaultSubscriber<>(true, "SelfConcatWith"));
		
		
		ThreadUtils.sleep(4100);
		System.out.println("main dies");
		
	}
}
