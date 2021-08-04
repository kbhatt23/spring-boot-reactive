package com.learning.flux_basics.chapters;

import java.util.Arrays;
import java.util.List;

import com.learning.mono_basics.utils.MonoStreamsUtils;
import com.learning.mono_basics.utils.ThreadUtils;

import reactor.core.publisher.Flux;

public class FluxJustEager {

	public static void main(String[] args) {
		
		//just method is eager meaning even though no subsrciber it still perpares the data whihc is time consuming
		Flux<String> data = Flux.just(names());
		
		
		data.subscribe(MonoStreamsUtils :: printOnNext,
				MonoStreamsUtils :: printOnError,
				MonoStreamsUtils :: printOnComplete
				);
	
	}
	
	public static String names() {
		System.out.println("find names started");
		
		ThreadUtils.sleep(2000);
		
		return "jai shree ram";
	}
	
}
