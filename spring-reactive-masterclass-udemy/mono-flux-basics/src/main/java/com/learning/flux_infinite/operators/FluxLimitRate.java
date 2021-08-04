package com.learning.flux_infinite.operators;

import com.learning.mono_basics.utils.DefaultSubscriber;

import reactor.core.publisher.Flux;

//limitrate is a buffer of defines size
//it calls size items for first time and once subscriber takes 75 precent data out
//buffer calls another 75 items frm publisher ...process continues
public class FluxLimitRate {

	public static void main(String[] args) {
		
		Flux.range(1, 1000)
			.log()
		    .limitRate(100) // 100 will be buffer size
		    .subscribeWith(new DefaultSubscriber<>(true, FluxLimitRate.class.getSimpleName()));
	}
	
}
