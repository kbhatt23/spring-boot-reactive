package com.learning.mono_flux_advanced.utils;

import java.util.concurrent.CountDownLatch;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import lombok.extern.slf4j.Slf4j;

public class CountDownSubscriber<T> implements Subscriber<T>{

	private final boolean enableLogs;
	
	private final String name;
	
	private Subscription subscription;
	
	private final  CountDownLatch countDownLatch;
	
	public CountDownSubscriber(boolean enableLogs, String name , CountDownLatch countDownLatch) {
		this.enableLogs = enableLogs;
		this.name = name;
		this.countDownLatch=countDownLatch;
	}

	@Override
	public void onSubscribe(Subscription s) {
		
		if(enableLogs) {
			System.out.println("Thread: "+Thread.currentThread().getName()+ " ThreadSubscriber.onSubscribe: "+ name +" recieves subscription.");
		}
		this.subscription = s;
		s.request(Long.MAX_VALUE);
	}

	@Override
	public void onNext(T t) {
		System.out.println("Thread: "+Thread.currentThread().getName() + " ThreadSubscriber.onNext: "+ name +" recieves message: "+t);
	}

	@Override
	public void onError(Throwable t) {
		if(enableLogs) {
			System.out.println("Thread: "+Thread.currentThread().getName()+" ThreadSubscriber.onError: "+ name +" recieves error: "+t.getMessage());
		}
		
		countDownLatch.countDown();
	}

	@Override
	public void onComplete() {
		if(enableLogs) {
			System.out.println("Thread: "+Thread.currentThread().getName()+" ThreadSubscriber.onComplete: "+ name +" completes task.");
		}
		
		countDownLatch.countDown();
	}

}
