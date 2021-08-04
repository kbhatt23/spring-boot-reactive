package com.learning.flux_basics.chapters;

import java.time.Duration;

import com.learning.mono_basics.utils.MonoStreamsUtils;
import com.learning.mono_basics.utils.ThreadUtils;

import reactor.core.publisher.Flux;

public class FluxIntervals {

	public static void main(String[] args) {
		Flux.interval(Duration.ofSeconds(1)) // pass value every one second
				//starts with 0
		.subscribe(MonoStreamsUtils :: printOnNext,
				MonoStreamsUtils :: printOnError,
				MonoStreamsUtils :: printOnComplete
				);
		
		//it will be async an non blocking so need to stop main 
		ThreadUtils.sleep(5000);
	}
}
