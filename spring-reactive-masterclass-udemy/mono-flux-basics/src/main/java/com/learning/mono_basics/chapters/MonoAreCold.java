package com.learning.mono_basics.chapters;

import com.learning.mono_basics.utils.MathUtils;
import com.learning.mono_basics.utils.ThreadUtils;

import reactor.core.publisher.Mono;

public class MonoAreCold {

	public static void main(String[] args) {
		Mono.fromSupplier(MonoAreCold :: findNumber)
			.filter(MathUtils :: isEven)
			.map(i ->{
				ThreadUtils.sleep(2000);
				return i * 2;
			})
			//.log()
			//untill we subscribe publisher do not send data to subscriber
			//it starts emitting after subscription
			.subscribe(System.out::println)
			;

			//however since main diest the async daemon thread ignores the task
		System.out.println("subscription done");
		
		ThreadUtils.sleep(2000);
		System.out.println("main dies");
	}
	
	public static int findNumber()
	{
		return  2;
	}
}
