package com.learning.flux_infinite.chapters;

import com.learning.mono_basics.utils.DefaultSubscriber;
import com.learning.mono_basics.utils.MonoStreamsUtils;
import com.learning.mono_basics.utils.ThreadUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

//keep on iterating until first name with c exists
//or else try this only until 5 elements -> after that give error
public class FluxGenerateWithState {

	public static void main(String[] args) {

		Flux.generate(FluxGenerateWithState :: initialState,
				FluxGenerateWithState :: iterateNext
				)
		//.take(2) // automatically cancels the publisher
		.subscribeWith(new DefaultSubscriber<>(true, FluxGenerateWithState.class.getSimpleName()))
		;
		
		 
	}
	
	//a callable interface
	private static int initialState() throws Exception{
		
		System.out.println("initialState called");
		return 0;
	}
	
	private static int iterateNext(int currentState , SynchronousSink<String> synchronousSink) {
		//System.out.println("iterateNext called");
		
		//time consuming task
		ThreadUtils.sleep(1000);
		
		String fullName = MonoStreamsUtils.FAKER.name().fullName();
		
		synchronousSink.next(fullName);
		
		if(fullName.toLowerCase().startsWith("c")) {
			synchronousSink.complete();
		}
		if(++currentState == 10) {
			synchronousSink.error(new IllegalStateException("generate attempts are complete"));
		}
		
		return currentState;
	}
	
}
