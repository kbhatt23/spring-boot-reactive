package com.learning.flux_basics.chapters;

import java.util.List;

import com.learning.mono_basics.utils.MonoStreamsUtils;

import reactor.core.publisher.Flux;

public class StreamsVsList {

	public static void main(String[] args) {
		
		int count=10;
		//useList(count);
		
		//still will wait as until list is preapred we can not start the stream publisher
		//useITerableStream(count);
		
		
		useStreamsSmartly(count);
			
	}

	private static void useITerableStream(int count) {
		Flux.fromIterable(MonoStreamsUtils.generateNames(count))
		.subscribe(MonoStreamsUtils :: printOnNext,
				MonoStreamsUtils :: printOnError,
				MonoStreamsUtils :: printOnComplete
				);
	}

	private static void useStreamsSmartly(int count) {
		Flux.range(1, count)
			.map(i -> MonoStreamsUtils.generateName())
			.subscribe(MonoStreamsUtils :: printOnNext,
			MonoStreamsUtils :: printOnError,
			MonoStreamsUtils :: printOnComplete
			);
	}

	private static void useList(int count) {
		List<String> generateNames = MonoStreamsUtils.generateNames(count);
		
		generateNames.forEach(System.out::println);
	}
	
	
}
