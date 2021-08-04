package com.learning.mono_flux_advanced.sinks.challenges;

import com.learning.mono_flux_advanced.utils.ThreadUtils;

import reactor.core.publisher.Flux;
import reactor.util.context.Context;

public class UserRateRunner {

	public static void main(String[] args) {
		String username = "kanishk";
		
		Flux<String> watchMovieStream = watchMovieStream();
		
		watchMovieStream
		.doOnComplete(() -> UserRateService.increaseCount(username))
		.repeat(4) // resubscribe once oncompele happens
		.contextWrite(Context.of(UserRateService.USERNAME, username))
		
		.subscribe(
				movieStream -> System.out.println("user "+username+ " watching "+movieStream)
				,err -> System.out.println("user "+username +" recieves error "+err.getMessage())
				, () -> {
					System.out.println("user "+username+" completes movie stream");
					System.out.println("===============================");
				}
				);
		
	}

	// assume eery few seond string stream of movie is passed to customer
	private static Flux<String> watchMovieStream(){
		
	Flux<String> validFlux = Flux.create(fluxSink -> {
			for(int i = 0 ; i < 4 ; i++) {
				if(fluxSink.isCancelled())
					break;
				ThreadUtils.sleep(500);
				fluxSink.next("movie scene "+i);
			}
			//movie completed
			fluxSink.complete();
		});
		
		return Flux.deferContextual(context -> {
			
			String username = context.get(UserRateService.USERNAME);
			if(UserRateService.userExists(username)) {
				if(UserRateService.isUserCountWithinLimit(username)) {
					return validFlux;
				}else {
					return Flux.error(() -> new IllegalArgumentException("username max view completed"));
				}
				
			}else {
				return Flux.error(() -> new IllegalArgumentException("username "+username+" does not exist"));
			}
			
		});
		
	
	}

}
