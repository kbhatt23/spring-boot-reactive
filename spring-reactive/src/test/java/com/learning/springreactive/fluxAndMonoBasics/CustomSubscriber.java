package com.learning.springreactive.fluxAndMonoBasics;

import org.reactivestreams.Subscription;

import reactor.core.publisher.BaseSubscriber;

public class CustomSubscriber extends BaseSubscriber<Integer>{


	/*
	 * @Override protected void hookOnNext(Integer value) { //on every OOB onrequest
	 * event request for 1 //this is default behaviour only request(1); if(value ==
	 * 4) { //cancel events sending after 4 //others will be skipped form both
	 * publisher and subscriber cancel(); } }
	 */
	
	@Override
	protected void hookOnNext(Integer value) {
		System.out.println("entry fetched "+value);
		super.hookOnNext(value);
	}
	
	@Override
	protected void hookOnSubscribe(Subscription subscription) {
		System.out.println("subscription added");
		super.hookOnSubscribe(subscription);
	}
	
	@Override
	protected void hookOnCancel() {
		System.err.println("cancelling all events");
	}
	
	@Override
	protected void hookOnComplete() {
		System.out.println("completed all events");
		}
	
	@Override
	protected void hookOnError(Throwable throwable) {
		System.err.println("Error occurred "+throwable);
	}

}
