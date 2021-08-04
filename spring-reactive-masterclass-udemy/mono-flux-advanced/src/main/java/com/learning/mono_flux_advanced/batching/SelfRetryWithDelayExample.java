package com.learning.mono_flux_advanced.batching;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

import com.learning.mono_flux_advanced.utils.CountDownSubscriber;
import com.learning.mono_flux_advanced.utils.MonoStreamsUtils;
import com.learning.mono_flux_advanced.utils.ThreadUtils;

import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

public class SelfRetryWithDelayExample {

	public static void main(String[] args) {

		Flux<String> create = Flux.generate(synchronousSink -> {
			ThreadUtils.sleep(1000);

			Boolean isSuccess = MonoStreamsUtils.FAKER.random().nextBoolean();
			System.out.println("Name generation started with status "+isSuccess);
			if (!isSuccess) {
				synchronousSink.error(new RuntimeException("unable to generate name"));
			} else {
				synchronousSink.next(MonoStreamsUtils.FAKER.funnyName().name());
			}
		});

		CountDownLatch countDownLatch = new CountDownLatch(1);
		create
		//.retry(2)
		.retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(2))) // in caes of error retry after 2 seconds and try 2 time extra
		.take(2)
		   .subscribe(new CountDownSubscriber(true, "SelfRetryExample", countDownLatch));
		
		
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	
		System.out.println("main dies");
	}
}
