package com.learning.mono_flux_advanced.combiningpublishers;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

import com.learning.mono_flux_advanced.utils.CountDownSubscriber;
import com.learning.mono_flux_advanced.utils.DefaultSubscriber;
import com.learning.mono_flux_advanced.utils.MathUtils;
import com.learning.mono_flux_advanced.utils.MonoStreamsUtils;
import com.learning.mono_flux_advanced.utils.ThreadUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import reactor.core.scheduler.Schedulers;

public class CarPriceChallenge {

	public static void main(String[] args) {
		
		Flux<Double> priceStream = Flux.generate(() -> 20000d, CarPriceChallenge :: updatePrice).subscribeOn(Schedulers.boundedElastic());
		
		//priceStream.subscribe(new DefaultSubscriber(true, "car price"));
		
		Flux<Double> demandStream = Flux.generate(CarPriceChallenge :: randomRate).subscribeOn(Schedulers.boundedElastic());
		
		//demandStream.subscribe(new DefaultSubscriber<>(true, "demand rate"));
		
		CountDownLatch countDownLatch = new CountDownLatch(1);
		Flux.combineLatest(items ->{
			double price = (double) items[0];
			double demandRate = (double) items[1];
			
			return price * demandRate;
		}, priceStream , demandStream)
		.publishOn(Schedulers.parallel())
		.subscribe(new CountDownSubscriber<>(true, "CarPriceChallenge", countDownLatch))
		;
		
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("main diest");
	}
	

	private static double updatePrice(double currentPrice , SynchronousSink<Double> synchronousSink) {
		
		//every one month or one second here
		ThreadUtils.sleep(1000);
		
		synchronousSink.next(currentPrice);
		
		return currentPrice -100;
	}
	
	private static void randomRate(SynchronousSink<Double> synchronousSink) {
		
		//every one month or one second here
		ThreadUtils.sleep(4000);
		
		synchronousSink.next(MathUtils.getRandomNumber(0.7, 1.4));
		
	}
	
}
