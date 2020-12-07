package com.learning.springreactive;

import java.time.Duration;

import org.junit.Test;

import com.learning.springreactive.customsubscribers.CustomStringSubscriberHook;

import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class BackPressureEnabledTest extends BaseTest{

	@Test
	public void basicBackPressure() {
		Flux<String> strNumbersFlux = Flux.range(1, 5)
			.map(String::valueOf)
			.log()
			
		;
		
		strNumbersFlux.subscribe(BackPressureEnabledTest::findSuccessConsumer, BackPressureEnabledTest::findErrorConsumer,
				BackPressureEnabledTest::findCompletedConsumer, subsription -> subsription.request(3));
	}
	
	@Test
	public void basicBackPressureStepverifier() {
		Flux<String> strNumbersFlux = Flux.range(1, 5)
			.map(String::valueOf)
			.log()
			
		;
		StepVerifier.create(strNumbersFlux)
					.expectSubscription()
					.expectNext("1")
					.expectNext("2")
					.expectNext("3")
					.thenCancel()
					.verify();
	}
	
	@Test
	public void customBackPressure() {
		Flux<String> strNumbersFlux = Flux.range(1, 5)
			.map(String::valueOf)
			.log()
		;
		strNumbersFlux.subscribe(new CustomStringSubscriberHook());
	}
}
