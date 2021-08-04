package com.learning.mono_flux_advanced.combiningpublishers;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

import com.learning.mono_flux_advanced.utils.CountDownSubscriber;

import reactor.core.publisher.Flux;

public class CombineLatest {

	public static void main(String[] args) {
		
		CountDownLatch countDownLatch = new CountDownLatch(1);
		//merges data based on latest item
		//if fluxA has 2 data and fluxB have 0 it waits
		//the moment fluxB get something it pushes this data and second data for fluxA(latest)
		//if fluxB recieves one more than that data along ith data 2 from fluxA is sent
		
		Flux<String> fluxA = Flux.just("A","B").delayElements(Duration.ofSeconds(1));
		
		Flux<String> fluxB = Flux.just("K","L","M","N").delayElements(Duration.ofMillis(2100));
		
		
		Flux.combineLatest(items -> items[0]+" , "+items[1], fluxA, fluxB)
			.subscribe(new CountDownSubscriber<>(true, "CombineLatest", countDownLatch));
		
		
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("main dies");
	}
}
