package com.learning.flux_infinite.chapters;

import com.learning.mono_basics.utils.DefaultSubscriber;

import reactor.core.publisher.Flux;

public class TakeClosesPublisher {

	public static void main(String[] args) {
		Flux.range(1, 10) // has 10 data in publisher
		    .log("upstream : ")
			.take(5) // we want only 5 out of total 10 data -> automatically the publisher stops data sending(cancelled is called)
			.log("downstream : ")
			.subscribeWith(new DefaultSubscriber<>(true, TakeClosesPublisher.class.getSimpleName()));
	}
}
