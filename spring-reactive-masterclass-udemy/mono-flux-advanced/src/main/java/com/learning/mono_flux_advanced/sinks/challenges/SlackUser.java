package com.learning.mono_flux_advanced.sinks.challenges;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import lombok.Data;

@Data
public class SlackUser<T> implements Subscriber<T>{

	private String userName;
	
	
	public SlackUser(String userName) {
		this.userName = userName;
	}


	@Override
	public void onSubscribe(Subscription s) {
		s.request(Long.MAX_VALUE);
	}


	@Override
	public void onNext(T t) {
		System.out.println("user: "+userName + " recieves message "+t);
	}


	@Override
	public void onError(Throwable t) {
		
	}


	@Override
	public void onComplete() {
		System.out.println("user: "+userName+" completes slack channel");
	}
}

