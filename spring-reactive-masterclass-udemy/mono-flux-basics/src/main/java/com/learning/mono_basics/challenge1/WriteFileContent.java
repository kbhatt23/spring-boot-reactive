package com.learning.mono_basics.challenge1;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.learning.mono_basics.utils.MonoStreamsUtils;

import reactor.core.publisher.Mono;

public class WriteFileContent {

	public static void main(String[] args) {
		
		Stream<String> dataStream = Stream.of("sita ram","uma mahesh", "radhe krishna","parvati pati");
		
		String data = dataStream.collect(Collectors.joining(","));
		
		FileService fileService = new FileService("files/movies.txt",data);
		
		Mono<Object> fromRunnable = Mono.fromRunnable(fileService :: writeFile);
		
		//no subscriber means laziness and hence nothing happens on the publisher side
		//no on next event and directly oncomplete event will come as it is runnable
		//subscribe by default will be blocking
		fromRunnable.subscribe(MonoStreamsUtils :: printOnNext,
				MonoStreamsUtils :: printOnError,
				MonoStreamsUtils :: printOnComplete
				);
	}
}
