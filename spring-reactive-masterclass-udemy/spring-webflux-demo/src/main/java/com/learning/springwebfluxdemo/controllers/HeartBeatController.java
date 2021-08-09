package com.learning.springwebfluxdemo.controllers;

import java.time.Duration;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

//demo for a cold publisher and hot publisher
@RestController
public class HeartBeatController {

	@GetMapping("/cold")
	public Flux<String> cold(){
		return Flux.interval(Duration.ofSeconds(1))
					.map(i -> "jai shree ram "+i);
	}
	
	//full ht 
	//start sending data even though no subscribers
	@GetMapping("/hot")
	public Flux<String> hot(){
		return Flux.interval(Duration.ofSeconds(1))
					.map(i -> "jai shree ram "+i)
					.share()
					;
	}
}
