package com.learning.flux_basics.chapters;

import java.util.concurrent.atomic.AtomicReference;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import com.learning.mono_basics.utils.ThreadUtils;

import reactor.core.publisher.Flux;

public class FluxCustomSubscriber {

	public static void main(String[] args) {
		
		Flux<Integer> dataFlux = Flux.range(1, 20);
		
		//atomic object
		AtomicReference<Subscription> subscriptionRef = new AtomicReference<>();

		dataFlux
			.subscribeWith(new Subscriber<Integer>() {

				@Override
				public void onSubscribe(Subscription s) {
					System.out.println("FluxCustomSubscriber.onSubscribe called with subscription "+s);
					subscriptionRef.set(s);
					
					//we will call the data using request later using main thread for demonstration
				}

				@Override
				public void onNext(Integer t) {
					System.out.println("FluxCustomSubscriber.onNext Data recieved "+t);
				}

				@Override
				public void onError(Throwable t) {
					System.out.println("FluxCustomSubscriber.onError error occurred "+t);
				}

				@Override
				public void onComplete() {
					System.out.println("FluxCustomSubscriber.onComplete called ");
				}
				
			});
		
		//remember until subscriber request for data using subscrption , data is not sent from publisher to subscriber
		Subscription subscription = subscriptionRef.get();
		
		subscription.request(5);
		//it will give only 5 data with no oncomplete
		
		//back pressure support
		ThreadUtils.sleep(2000);
		
		subscription.request(5);
		
		ThreadUtils.sleep(2000);
		
		//lets cancel now as we are overloaded
		subscription.cancel();
		
		System.out.println("FluxCustomSubscriber main dies ");
	}
}
