package com.learning.mono_flux_advanced.combiningpublishers;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

import com.learning.mono_flux_advanced.utils.CountDownSubscriber;

import reactor.core.publisher.Flux;

//size of final flux is size of smaller flux
//index of these flux items are maintained
public class SelfFluxZip {

	public static void main(String[] args) {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		
		Flux<Integer> fluxA = Flux.range(1, 4).delayElements(Duration.ofSeconds(1));
		
		Flux<Integer> fluxB = Flux.range(10, 7).delayElements(Duration.ofSeconds(2));
		
//		fluxA.zipWith(fluxB, (first,second) -> {
//			return Math.max(first, second);
//		})
		
		fluxA.zipWith(fluxB, (a,b) -> a+" , "+b)
		 .subscribe(new CountDownSubscriber<>(true, "SelfFluxZip", countDownLatch))
		;
		
		
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("main dies");
	}
}
