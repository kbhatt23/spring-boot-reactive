package com.learning.mono_flux_advanced.sinks.challenges;

import com.learning.mono_flux_advanced.utils.DefaultSubscriber;

import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.Many;

public class LEarning {
public static void main(String[] args) {
	Many<String> sink = Sinks.many().replay().all();
	
	sink.asFlux().subscribe(new DefaultSubscriber(true, "user1"));
	sink.asFlux().subscribe(new DefaultSubscriber(true, "user2"));
	
	sink.tryEmitNext("jai shree ram");
}
}
