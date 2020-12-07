package com.learning.springreactive;

import java.io.IOException;
import java.time.Duration;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
//genrally when error occurs -> one onError event is sent and oncmplete is never snet
// the methods onerrorResume and onErrorReturn gracefully returns one value in case of error , this way we also recieve oncomplete even

//in case of erro -> onNext(1) , onNext(2).....onError() -> and no on complete

//in case of graceful handling of error -> onNext(1) , onNext(2).....onNext(default vlaue) -> oncomplete()
public class ErrorHandlingTests extends BaseTest{

	@Test
	public void erroScenarioTestExpecation() {
		
		Flux<String> strFlux = Flux.just("sita-ram","radhe-shyam")
				.concatWith(Flux.error(() -> new RuntimeException("Str Flux exits woth exception")))
				.concatWith(Flux.just("uma-shankar","radha-damodar")).log()		
						;
		
		//old way to verify the test
		StepVerifier.create(strFlux).expectSubscription()
								.expectNext("sita-ram","radhe-shyam")
								.expectError(RuntimeException.class)
								.verify();
								
	}
	@Test
	public void errorScenarioHandle1() {
		
		Flux<String> strFlux = Flux.just("sita-ram","radhe-shyam")
				.concatWith(Flux.error(() -> new RuntimeException("Str Flux exits with exception")))
				.concatWith(Flux.just("uma-shankar","radha-damodar"))
				.onErrorResume((e) ->{
					System.out.println("Exception occurred with message "+e.getMessage());
					return Flux.just("jai shree ram will handle any error");
				})
				.log()		
						;
		
		//old way to verify the test
		StepVerifier.create(strFlux).expectSubscription()
								.expectNext("sita-ram","radhe-shyam")
								//since we have error handle, whenever any error is found influx it returns default message
								//.expectError(RuntimeException.class)
								.expectNext("jai shree ram will handle any error")
								.verifyComplete();
								
	}
	
	@Test
	public void errorScenarioHandle2() {
		
		Flux<String> strFlux = Flux.just("sita-ram","radhe-shyam")
				.concatWith(Flux.error(() -> new RuntimeException("Str Flux exits with exception")))
				.concatWith(Flux.just("uma-shankar","radha-damodar"))
				.onErrorReturn("default jai shree ram")
				.log()		
						;
		
		//old way to verify the test
		StepVerifier.create(strFlux).expectSubscription()
								.expectNext("sita-ram","radhe-shyam")
								//since we have error handle, whenever any error is found influx it returns default message
								//.expectError(RuntimeException.class)
								.expectNext("default jai shree ram")
								.verifyComplete();
								
	}
	
	@Test
	public void erroScenarioTestExpecation_retry1() {
		
		Flux<String> strFlux = Flux.just("sita-ram","radhe-shyam")
				.concatWith(Flux.error(() -> new RuntimeException("Str Flux exits woth exception")))
				.concatWith(Flux.just("uma-shankar","radha-damodar"))
				.retry(2)
				.log()		
						;
		
		//old way to verify the test
		StepVerifier.create(strFlux).expectSubscription()
								.expectNext("sita-ram","radhe-shyam")
								.expectNext("sita-ram","radhe-shyam")
								.expectNext("sita-ram","radhe-shyam")
								.expectError(RuntimeException.class)
								.verify();
								
	}
	@Test
	public void erroScenarioTestExpecation_retry2() {
		
		Flux<String> strFlux = Flux.just("sita-ram","radhe-shyam")
				.concatWith(Flux.error(() -> new RuntimeException("Str Flux exits woth exception")))
				.concatWith(Flux.just("uma-shankar","radha-damodar"))
				.retry(2)
				.onErrorResume( e ->{
					System.out.println("Excption occurer in str Flux "+e.getMessage());
					return Flux.just("jai shree ram","jai radhe krishna");
				})
				.log()		
						;
		
		//old way to verify the test
		StepVerifier.create(strFlux).expectSubscription()
								.expectNext("sita-ram","radhe-shyam")
								.expectNext("sita-ram","radhe-shyam")
								.expectNext("sita-ram","radhe-shyam")
								.expectNext("jai shree ram","jai radhe krishna")
								.verifyComplete();
								
	}
	
	@Test
	public void erroScenarioTestExpecation_retry3() {
		
		Flux<String> strFlux = Flux.just("sita-ram","radhe-shyam")
				.concatWith(Flux.error(() -> new RuntimeException("Str Flux exits woth exception")))
				.concatWith(Flux.just("uma-shankar","radha-damodar"))
				.retry(2)
				.onErrorReturn( "bacho lo narayan"
				)
				.log()		
						;
		
		//old way to verify the test
		StepVerifier.create(strFlux).expectSubscription()
								.expectNext("sita-ram","radhe-shyam")
								.expectNext("sita-ram","radhe-shyam")
								.expectNext("sita-ram","radhe-shyam")
								.expectNext("bacho lo narayan")
								.verifyComplete();
								
	}
	
	//this methid is used to call the retyr only after some time
	//this helps allow third party to recover
	@Test
	public void erroScenarioTestExpecation_retry3_backoff() {
		
		Flux<String> strFlux = Flux.just("sita-ram","radhe-shyam")
				.concatWith(Flux.error(() -> new RuntimeException("Str Flux exits woth exception")))
				.concatWith(Flux.just("uma-shankar","radha-damodar"))
				.retryBackoff(2, Duration.ofSeconds(1))
				.onErrorReturn( "bacho lo narayan"
				)
				.log()		
						;
		
		//old way to verify the test
		StepVerifier.create(strFlux).expectSubscription()
								.expectNext("sita-ram","radhe-shyam")
								.expectNext("sita-ram","radhe-shyam")
								.expectNext("sita-ram","radhe-shyam")
								.expectNext("bacho lo narayan")
								.verifyComplete();
								
	}
	
}
