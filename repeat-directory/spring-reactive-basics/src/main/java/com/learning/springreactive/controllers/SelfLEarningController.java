package com.learning.springreactive.controllers;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.springreactive.bean.InnerBean;
import com.learning.springreactive.bean.SampleBean;

import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/learning")
public class SelfLEarningController {

	@GetMapping(value = "/test", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<String> testStrings() {
		return Flux.just("sita-ram", "radhe-krishna", "uma-shankar").delayElements(Duration.ofSeconds(1)).log();
	}

	@GetMapping(value = "/test-int", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Integer> testInts() {
		return Flux.just(1, 2, 3, 4, 5).delayElements(Duration.ofSeconds(1)).log();
	}
	
	@GetMapping(value = "/blocked-int")
	public Flux<Integer> blockedInts() {
		return Flux.just(1, 2, 3, 4, 5).delayElements(Duration.ofSeconds(1)).log();
	}

	@GetMapping("/empty")
	public Flux<Object> emptyFlux() {
		return Flux.empty().delayElements(Duration.ofSeconds(1)).log();
	}

	@GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE, value = "/json")
	public Flux<SampleBean> fluxBeanJson() {

		return Flux.range(1, 5)
				.map(id -> new SampleBean(id, "sample-bean-" + id, "jai shree ram " + id,
						new InnerBean("inner-bean-" + id, "jai radhe krishna " + id)))
				.delayElements(Duration.ofSeconds(1)).log();
	}

	@GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE, value = "/mono-int")
	public Mono<Integer> monoInts() {
		return Mono.just(108).delayElement(Duration.ofSeconds(1));
	}

	@GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE, value = "/infinite-cold")
	public Flux<Integer> infiniteCold() {
		return Flux.interval(Duration.ofSeconds(1))
				.map(Long::intValue)
				.log();
	}
	@GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE, value = "/infinite-hot")
	public Flux<Integer> infiniteHot() {
		 ConnectableFlux<Integer> publish = Flux.interval(Duration.ofSeconds(1))
				.map(Long::intValue)
				.log().publish();
		 //hot stream starts emititng now itslef even though browser/consumer.subscriber is not attached
		 //it will loose the data initaly and anytime we start the consumer it will pick from latest and ont from begining unlike cold stream
		 publish.connect();
		 return publish;
	}
}
