package com.learning.mono_flux_advanced.schedulers;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import com.learning.mono_flux_advanced.utils.CountDownSubscriber;
import com.learning.mono_flux_advanced.utils.ThreadSubscriber;
import com.learning.mono_flux_advanced.utils.ThreadUtils;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class DataPArallelism {

	public static void main(String[] args) {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		long start = System.currentTimeMillis();
		//why not use different threads even for same subscriber
		Flux.range(1, 40)
		   .map( i -> {ThreadUtils.sleep(500); ; return i*2;})
		   .parallel(8)
		   .runOn(Schedulers.boundedElastic())
		   .subscribe(new CountDownSubscriber<>(true, "DataPArallelism", countDownLatch))
		   ;
	
		long end = System.currentTimeMillis();
		
		System.out.println("total time taken "+(end-start) +" ms.");
	}
}
