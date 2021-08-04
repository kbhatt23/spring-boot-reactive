package com.learning.mono_basics.challenge1;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.learning.mono_basics.utils.MonoStreamsUtils;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class DeleteFile {
public static void main(String[] args) {
	FileService fileService = new FileService("files/movies.txt");
	
	Mono<Object> fromRunnable = Mono.fromRunnable(fileService :: deleteFile);
	
	//if we do not subscribe it wont start the deleting process
	
	//subscribe is blocking for runnable
	//only oncomplete event will be sent as runnable can not return any data
	
	CountDownLatch latch = new CountDownLatch(1);
	
	fromRunnable
		.subscribeOn(Schedulers.boundedElastic()) // making it async and non blocking
	.subscribe(MonoStreamsUtils :: printOnNext,
			MonoStreamsUtils :: printOnError,
			() -> {latch.countDown(); ; MonoStreamsUtils.printOnComplete(); }
			);
	
	try {
		latch.await(4, TimeUnit.SECONDS);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	
	System.out.println("DeleteFile.main diest");
} 

}
