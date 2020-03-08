package com.learning.springreactive.fluxAndMonoBasics;

import java.time.Duration;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxErrorHandling {

	@Test
	public void fluxErrorChecking() {
		System.out.println("========Start fluxErrorChecking==========");
		Flux<String> fluxStr = Flux.just("A" , "B" , "C")
				.concatWith(Flux.error(new RuntimeException("error me bhee ram")))
				.concatWith(Flux.just("D", "E"));
				;
		
		StepVerifier.create(fluxStr.log())
					.expectSubscription()
					.expectNext("A" , "B" , "C")
					.expectError(RuntimeException.class)
					.verify();
					
		
	}
	
	@Test
	public void fluxErrorHandlingWithResume() {
		System.out.println("========Start fluxErrorHandlingWithResume==========");
		Flux<String> fluxStr = Flux.just("A" , "B" , "C")
				.concatWith(Flux.error(new RuntimeException("error me bhee ram")))
				.concatWith(Flux.just("D", "E"))
				//on any experion we can take expcetion and use it
				//also return defualt value here
				.onErrorResume((throwable) -> {
					System.out.println("flux: expection occurred "+throwable.getMessage());
					return Flux.just("default1" , "default2");
				})
				;
		
		StepVerifier.create(fluxStr.log())
					.expectSubscription()
					.expectNext("A" , "B" , "C")
					.expectNext("default1" , "default2")
					//will fail as we have hanbdled and given defualt response
					//.expectError(RuntimeException.class)
					.verifyComplete();
					
		
	}
	
	@Test
	public void fluxErrorHandlingWithReturn() {
		System.out.println("========Start fluxErrorHandlingWithReturn==========");
		Flux<String> fluxStr = Flux.just("A" , "B" , "C")
				.concatWith(Flux.error(new RuntimeException("error me bhee ram")))
				.concatWith(Flux.just("D", "E"))
				//can not get Exception object that caused the issue
				//just return defualt value here
				//can not retyrn a flux herer
				//can only return single entry
				.onErrorReturn("default3");
				;
		
		StepVerifier.create(fluxStr.log())
					.expectSubscription()
					.expectNext("A" , "B" , "C")
					.expectNext("default3")
					//will fail as we have hanbdled and given defualt response
					//.expectError(RuntimeException.class)
					.verifyComplete();
					
		
	}
	
	
	
	@Test
	public void fluxErrorHandlingWithMap() {
		System.out.println("========Start fluxErrorHandlingWithMap==========");
		Flux<String> fluxStr = Flux.just("A" , "B" , "C")
				.concatWith(Flux.error(new RuntimeException("error me bhee ram")))
				.concatWith(Flux.just("D", "E"))
				.onErrorMap((error) -> {
					System.err.println("erro agaya with message "+error.getMessage() );
					return new CustomException(error.getMessage());
				})
					
				;
		
		StepVerifier.create(fluxStr.log())
					.expectSubscription()
					.expectNext("A" , "B" , "C")
					//no defualt value here
					//.expectNext("default3")
					//will fail as we have hanbdled and given defualt response
					.expectError(CustomException.class)
					.verify();
					
		
	}
	
	@Test
	public void fluxErrorHandlingWithRetry() {
		System.out.println("========Start fluxErrorHandlingWithRetry==========");
		Flux<String> fluxStr = Flux.just("A" , "B" , "C")
				.concatWith(Flux.error(new RuntimeException("error me bhee ram")))
				.concatWith(Flux.just("D", "E"))
				//will retry twice if error cocured
				.retry(2)
				.onErrorMap((error) -> {
					System.err.println("erro agaya with message "+error.getMessage() );
					return new CustomException(error.getMessage());
				})
					
				;
		
		StepVerifier.create(fluxStr.log())
					.expectSubscription()
					.expectNext("A" , "B" , "C")
					//retry entries
					.expectNext("A" , "B" , "C")
					.expectNext("A" , "B" , "C")
					//no defualt value here
					//.expectNext("default3")
					//will fail as we have hanbdled and given defualt response
					.expectError(CustomException.class)
					.verify();
					
		
	}
	
	@Test
	public void fluxErrorHandlingWithRetryAndBackOff() {
		System.out.println("========Start fluxErrorHandlingWithRetryAndBackOff==========");
		Flux<String> fluxStr = Flux.just("A" , "B" , "C")
				.concatWith(Flux.error(new RuntimeException("error me bhee ram")))
				.concatWith(Flux.just("D", "E"))
				//will retry twice if error cocured
				//back off means how much time to wait after error and before retrying again
				//will wait for 2 seconds after error and before retrying
				.retryBackoff(2, Duration.ofSeconds(2))
				.onErrorMap((error) -> {
					System.err.println("erro agaya with message "+error.getMessage() );
					return new CustomException(error.getMessage());
				})
					
				;
		
		StepVerifier.create(fluxStr.log())
					.expectSubscription()
					.expectNext("A" , "B" , "C")
					//retry entries
					.expectNext("A" , "B" , "C")
					.expectNext("A" , "B" , "C")
					//no defualt value here
					//.expectNext("default3")
					//will fail as we have hanbdled and given defualt response
					.expectError(CustomException.class)
					.verify();
					
		
	}
	
	
	
}
