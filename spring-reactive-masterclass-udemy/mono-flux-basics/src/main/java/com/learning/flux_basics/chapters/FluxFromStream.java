package com.learning.flux_basics.chapters;

import java.util.stream.Stream;

import com.learning.mono_basics.utils.MonoStreamsUtils;

import reactor.core.publisher.Flux;

public class FluxFromStream {

	public static void main(String[] args) {
		Stream<String> namesStream = Stream.of("sita ram","uma mahesh","radhe krishna","parvati pati");
		
		//any intermediate operation on a steam gives new object
		//if using same refercen we call method in sperate line  it fails in second time
		
		//need to close these lines
//		namesStream
//		.map(String::toUpperCase) // returns a new stream and hence for each is applicable
//		.forEach(System.out::println);
		
		//we are calling the same stream object now whihc is already closed in previos lines
//		namesStream
//		.map(String::toUpperCase) // returns a new stream and hence for each is applicable
//		.forEach(System.out::println);
		
		System.out.println("=========flux starts==================");
		
		//even now the stream is already used once while printing so should give same error as line 19
		Flux<String> fromStream = Flux.fromStream(namesStream);
		
		fromStream.subscribe(MonoStreamsUtils :: printOnNext,
				MonoStreamsUtils :: printOnError,
				MonoStreamsUtils :: printOnComplete
				);
		
		//but if we subscribe again , the stream is already used so again error comes
		System.out.println("=========flux starts again ==================");
		
		fromStream.subscribe(MonoStreamsUtils :: printOnNext,
				MonoStreamsUtils :: printOnError,
				MonoStreamsUtils :: printOnComplete
				);
	} 
	
}
