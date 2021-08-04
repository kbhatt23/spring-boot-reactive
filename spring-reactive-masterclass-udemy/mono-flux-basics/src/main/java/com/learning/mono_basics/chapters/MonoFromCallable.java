package com.learning.mono_basics.chapters;

import java.util.Random;

import com.learning.mono_basics.utils.MonoStreamsUtils;
import com.learning.mono_basics.utils.ThreadUtils;

import reactor.core.publisher.Mono;

//callable and subscriber are similar -> takes no input but returns one output
//however in clable we can throw checked exceptions
public class MonoFromCallable {

	private static final Random RANDOM = new Random();
	public static void main(String[] args) throws Exception {
		//eager , even though no subscriber it still try to perare data for publisher
		//Mono<String> data = Mono.just(MonoFromCallable . getName()); 
		
		//Mono.fromSupplier(MonoFromCallable :: getName); // wont work as it throws exception
		
		//this is also lazy so if we wont subscribe it do not preare data for publisher
		Mono<String> data = Mono.fromCallable(MonoFromCallable :: getName); // wont work as it throws exception
		
		data
		.subscribe(MonoStreamsUtils :: printOnNext,
				MonoStreamsUtils :: printOnError,
				MonoStreamsUtils :: printOnComplete
				);
		
		
	}
	
	// example of callable
	//no input one output but can throw checked exception
	//callable
	public static String getName() throws Exception{
		System.out.println("getName called");
		
		ThreadUtils.sleep(2000);
		boolean isSuccess = RANDOM.nextBoolean();
		if(!isSuccess)
			throw new Exception("exception occurred while creating name");
			
		return "sita ram";
				
				
	}
}
