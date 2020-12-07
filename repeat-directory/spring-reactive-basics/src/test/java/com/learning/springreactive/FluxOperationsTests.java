package com.learning.springreactive;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxOperationsTests extends BaseTest {

	@Test
	public void basicFilter() {
		Flux<String> itemsFlux = Flux.just("sita-ram", "radhe-shyam", "ramduta-hanuman");

		Flux<String> filteredFlux = itemsFlux.filter(item -> item.contains("ram")).log();

		filteredFlux.subscribe(FluxOperationsTests::findSuccessConsumer, FluxOperationsTests::findErrorConsumer,
				FluxOperationsTests::findCompletedConsumer);
	}

	@Test
	public void basicFilterTeest() {
		Flux<String> itemsFlux = Flux.just("sita-ram", "radhe-shyam", "ramduta-hanuman");

		Flux<String> filteredFlux = itemsFlux.filter(item -> item.contains("ram"));

		StepVerifier.create(filteredFlux).expectNext("sita-ram", "ramduta-hanuman").verifyComplete();
	}


	@Test
	public void basicFilterTeest1() {
		Flux<String> itemsFlux = Flux.just("sita-ram", "radhe-shyam", "ramduta-hanuman");

		// it retuns no item as all are filtered and hence direct on compelte event will
		// be sent
		Flux<String> filteredFlux = itemsFlux.filter(item -> item.contains("krishna")).log();

		StepVerifier.create(filteredFlux).verifyComplete();
	}

	@Test
	public void basicFilterTeetMono() {
		Mono<String> itemsMono = Mono.fromSupplier(() -> "sita-ram");

		// it retuns no item as all are filtered and hence direct on compelte event will
		// be sent
		Mono<String> filteredMono = itemsMono.filter(item -> item.contains("krishna")).log();

		StepVerifier.create(filteredMono).verifyComplete();
	}
}
