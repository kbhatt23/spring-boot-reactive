package com.learning.mono_basics.chapters;

import com.learning.mono_basics.utils.MonoStreamsUtils;

import reactor.core.publisher.Mono;

public class MonoSubscriptionUsingFaker {

	public static void main(String[] args) {
		Mono<String> data = MonoStreamsUtils.findUser(1);
		
		data.subscribe(MonoStreamsUtils :: printOnNext,
				MonoStreamsUtils :: printOnError,
				MonoStreamsUtils :: printOnComplete
				);
	}
}
