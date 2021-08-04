package com.learning.mono_flux_advanced;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;
import reactor.util.context.Context;

public class ContexstBasedTest {
	private static final String USER_DO_NOT_EXIST = "user do not exist";
	private static final String USERNAME = "username";
	private static Set<String> VALID_USERNAMES = new HashSet<>(Arrays.asList("kanishk","debu"));

	@Test
	public void contextFailureNoData() {
		StepVerifier.create(contextBasedFlux())
				.expectError();
			
	}
	
	@Test
	public void contextFailureWrongData() {
		String username = "kakuta";
		StepVerifierOptions withInitialContext = StepVerifierOptions.create().withInitialContext(Context.of(USERNAME, username));
		
		StepVerifier.create(contextBasedFlux(),withInitialContext)
				.expectSubscription()
				.expectErrorMatches(error -> error.getClass() == IllegalArgumentException.class && error.getMessage().equals(USER_DO_NOT_EXIST))
				.verify();
	}
	
	@Test
	public void contextFailureValidData() {
		String username = "kanishk";
		StepVerifierOptions withInitialContext = StepVerifierOptions.create().withInitialContext(Context.of(USERNAME, username));
		
		StepVerifier.create(contextBasedFlux(),withInitialContext)
				.expectSubscription()
				.assertNext(message -> assertTrue(message.equals("jai shree ram says "+username)))
				.verifyComplete();
	}
	
	private Mono<String> contextBasedFlux(){
		return Mono.deferContextual(contextView ->{
			if(contextView.hasKey(USERNAME) && VALID_USERNAMES.contains((String)contextView.get(USERNAME))) {
				return Mono.just("jai shree ram says "+contextView.get(USERNAME));
			}
			return Mono.error(() -> new IllegalArgumentException(USER_DO_NOT_EXIST));
		});
	}
}
