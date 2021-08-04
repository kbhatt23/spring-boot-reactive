package com.learning.flux_basics.chapters;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import com.learning.mono_basics.utils.MonoStreamsUtils;

import reactor.core.publisher.Flux;

public class FluxFromIterables {

	public static void main(String[] args) {
		String[] namesArray = {"sita ram","uma mahesh","radhe krishna","parvati pati"};
		List<String> names = Arrays.asList(namesArray);
		
		//Flux.fromIterable(names)
		Flux.fromArray(namesArray)
		.subscribe(MonoStreamsUtils :: printOnNext,
				MonoStreamsUtils :: printOnError,
				MonoStreamsUtils :: printOnComplete
				);
	}
}
