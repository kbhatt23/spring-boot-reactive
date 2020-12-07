package com.learning.springreactive.customsubscribers;

import org.reactivestreams.Subscription;

import reactor.core.publisher.BaseSubscriber;

public class CustomStringSubscriberHook extends BaseSubscriber<String>{

	
	@Override
	protected void hookOnNext(String value) {
		request(1);
		System.out.println("CustomStringSubscriberHook: Found element "+value);
		if(value.equals("4")) {
			//cancel can call cancel hook , and then no itmes will be sent nor onco[pleteevent will be sent
			cancel();
		}
	}
	
	
}
