package com.learning.springreactive.controller;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class FluxAndMonoController {

	@GetMapping("/flux")
	public Flux<Integer> returnFlux(){
		return Flux.just(1,2,3,4,5).log();
	}
	
	@GetMapping(value = "/fluxStream" , produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Integer> returnFluxStram(){
		//since the return is fast we can not see benefit of stream
		//we are adding delay so that stream goes out on basis of whatever is returned at that moment
		//eg : hystix stream
		return Flux.just(1,2,3,4,5).
				delayElements(Duration.ofSeconds(1))
				.log();
	}
	
	@GetMapping(value = "/fluxInfiniteCold" , produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Integer> fluxInfiniteCold(){
		return Flux.interval(Duration.ofSeconds(1))
					.map(l -> l.intValue())
					.log();
	}
	
	@GetMapping(value = "/fluxInfiniteHot" , produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public ConnectableFlux<Integer> fluxInfiniteHot(){
		
		 ConnectableFlux<Integer> connectableFlux = Flux.interval(Duration.ofSeconds(1))
					.map(l -> l.intValue())
					.publish()
					;
		 return connectableFlux;
		 
	}
	
	@GetMapping(value = "/mono")
	public Mono<Integer> monoData(){
		return Mono.just(1)
				.delayElement(Duration.ofSeconds(2))
				.log();
	}
	
	@GetMapping(value = "/monoStream" , produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Mono<Integer> monoStreamData(){
		return Mono.just(1)
				.delayElement(Duration.ofSeconds(2))
				.log();
	}
}
