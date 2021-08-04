package com.learning.flux_infinite.hotvscold;

import java.time.Duration;

import com.learning.mono_basics.utils.DefaultSubscriber;
import com.learning.mono_basics.utils.ThreadUtils;

import reactor.core.publisher.Flux;

public class ColdPublisher {

	public static void main(String[] args) {
		Flux<String> movies = Flux.interval(Duration.ofSeconds(2))
		    .map(i -> "movie-"+i)
		    ;
		
		ThreadUtils.sleep(4000);
		
		//publisher do nto start data transmission until subscribed by subscriber
		//cold by default
		movies.subscribe(new DefaultSubscriber<>(true, "kannu"));
		
		ThreadUtils.sleep(4000);
		
		//in cold pubisher channel is different for both
		movies.subscribe(new DefaultSubscriber<>(true, "mannu"));
		
		
		ThreadUtils.sleep(6000);
		
		System.out.println("main dies");
	}
}
