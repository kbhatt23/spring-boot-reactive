package com.learning.flux_infinite.hotvscold;

import java.time.Duration;

import com.learning.mono_basics.utils.DefaultSubscriber;
import com.learning.mono_basics.utils.ThreadUtils;

import reactor.core.publisher.Flux;

public class HotPublisherAutoconnect {

	public static void main(String[] args) {
		Flux<String> movies = Flux.range(1, 10).delayElements(Duration.ofSeconds(1))
		    .map(i -> "movie-"+i)
		    .publish()
		    .autoConnect(0) // start sending data even though no subscriber
		    ;
		
		ThreadUtils.sleep(4000);
		
		movies.subscribe(new DefaultSubscriber<>(true, "kannu"));
		
		ThreadUtils.sleep(4000);
		
		//in cold pubisher channel is different for both
		movies.subscribe(new DefaultSubscriber<>(true, "mannu"));
		
		
		ThreadUtils.sleep(6000);
		
		System.out.println("main dies");
	}
}
