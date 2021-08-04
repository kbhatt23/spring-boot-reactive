package com.learning.mono_flux_advanced.utils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import lombok.extern.slf4j.Slf4j;

public class DefaultSubscriber<T> implements Subscriber<T>{

	private final boolean enableLogs;
	
	private final String name;
	
	private Subscription subscription;
	
	public DefaultSubscriber(boolean enableLogs, String name) {
		this.enableLogs = enableLogs;
		this.name = name;
	}

	@Override
	public void onSubscribe(Subscription s) {
		
		if(enableLogs) {
			System.out.println("DefaultSubscriber.onSubscribe: "+ name +" recieves subscription.");
		}
		this.subscription = s;
		s.request(Long.MAX_VALUE);
	}

	@Override
	public void onNext(T t) {
		System.out.println("DefaultSubscriber.onNext: "+ name +" recieves message: "+t);
	}

	@Override
	public void onError(Throwable t) {
		if(enableLogs) {
			System.out.println("DefaultSubscriber.onError: "+ name +" recieves error: "+t.getMessage());
		}
		
	}

	@Override
	public void onComplete() {
		if(enableLogs) {
			System.out.println("DefaultSubscriber.onComplete: "+ name +" completes task.");
		}
	}

}
