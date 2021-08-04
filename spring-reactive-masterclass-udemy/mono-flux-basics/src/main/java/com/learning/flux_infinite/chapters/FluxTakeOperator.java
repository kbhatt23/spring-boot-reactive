package com.learning.flux_infinite.chapters;

import java.time.Duration;

import com.learning.mono_basics.utils.DefaultSubscriber;
import com.learning.mono_basics.utils.ThreadUtils;

import reactor.core.publisher.Flux;

public class FluxTakeOperator {
	public static void main(String[] args) {

		//infinite stream is always async
		Flux.interval(Duration.ofSeconds(1))
		  .take(4)
		  .subscribeWith(new DefaultSubscriber<>(true, FluxTakeOperator.class.getSimpleName()));
		
		
		ThreadUtils.sleep(10000);
		
	}
}
