package com.learning.flux_basics.challenges;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import reactor.core.publisher.Flux;

public class StockTickerService {
	private static final Random RANDOM = new Random();
	public static void main(String[] args) {
		Flux<Integer> stockPrice = stockPrice();
		
		CountDownLatch latch = new CountDownLatch(1);
		
		stockPrice.subscribeWith(new Subscriber<Integer>() {
			private AtomicReference<Subscription> subAtomicReference;
			@Override
			public void onSubscribe(Subscription s) {
				System.out.println("StockTickerService onSubscribe called");
				if(subAtomicReference == null) {
					subAtomicReference = new AtomicReference<Subscription>(s);
				}
				
				//lets also start the work
				//lets request the work unbounded
				s.request(Long.MAX_VALUE);
			}

			@Override
			public void onNext(Integer t) {
				System.out.println("Stock price recieved "+t);
				
				if( t > 110 || t < 90)
				{
					System.out.println("Stock price "+t +"out of range and hence closing it.");
					subAtomicReference.get().cancel();
					latch.countDown();
				}
			}

			@Override
			public void onError(Throwable t) {
				System.out.println("stock price got an error "+t.getMessage());
				latch.countDown();
			}

			@Override
			public void onComplete() {
				System.out.println("stock price monitoring completed sucesfully");
				latch.countDown();
			}
		});
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("StockTickerService.main dies");
	}
	
	public static Flux<Integer> stockPrice(){
		int low = 88;
		int high = 112;
		
		//remember flux.interval is async by default
		return Flux.interval(Duration.ofSeconds(1))
		     .map(i -> RANDOM.nextInt(high-low)+low)
		     .log()
		     ;
	}
}
