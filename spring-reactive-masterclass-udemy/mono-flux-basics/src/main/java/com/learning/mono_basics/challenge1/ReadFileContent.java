package com.learning.mono_basics.challenge1;

import java.io.IOException;

import com.learning.mono_basics.utils.MonoStreamsUtils;

import reactor.core.publisher.Mono;

public class ReadFileContent {

	public static void main(String[] args) {
		FileService fileService = new FileService("files/movies.txt");
		//for lazyness lets use supplier or 
		
		//readUsingEagerness(fileService);
		
		readUsingLaziness(fileService);
	}

	private static void readUsingLaziness(FileService fileService) {
		System.out.println("readUsingLaziness called");
		Mono<String> data = Mono.fromCallable(fileService :: readFile);
		
		//if we do not subscribe then data wont even be prepared for publisher
		
		data.subscribe(MonoStreamsUtils :: printOnNext,
		MonoStreamsUtils :: printOnError,
		MonoStreamsUtils :: printOnComplete
		);
	}

	//no subscriber still calling read file data
	private static void readUsingEagerness(FileService fileService) {
		System.out.println("readUsingEagerness called");
		try {
			Mono<String> data = Mono.just(fileService.readFile());
			
			//even if we do not subscribe the data is prepared for publisher
//			data.subscribe(MonoStreamsUtils :: printOnNext,
//					MonoStreamsUtils :: printOnError,
//					MonoStreamsUtils :: printOnComplete
//					);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
