package com.learning.mono_flux_advanced.contextusage;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.learning.mono_flux_advanced.utils.DefaultSubscriber;

import reactor.core.publisher.Mono;
import reactor.util.context.Context;

public class ContextFirstStep {

	private static final String USERNAME = "username";
	private static final Set<String> AUTHENTICATED_USERS ;
	static {
		AUTHENTICATED_USERS = new HashSet<>(Arrays.asList("kanishk","divyam"));
	}

	public static void main(String[] args) {
		
		sayHello()
		    //.contextWrite(Context.of(USERNAME, ""))
		//.contextWrite(Context.of(USERNAME, "messi"))
		.contextWrite(Context.of(USERNAME, "divyam"))
		.contextWrite(Context.of(USERNAME, ""))
			.subscribe(new DefaultSubscriber<>(true, "ContextFirstStep"));
	}
	
	//step 1
//	private static Mono<String> sayHello(){
//		return Mono.just("jai shree ram");
//	}
	
	//step 2
	//add security
	private static Mono<String> sayHello(){
		
		return Mono.deferContextual(contextView -> {
			//get data from context passed by subscriber
			String username = contextView.get(USERNAME);
			
			if(!StringUtils.isEmpty(username) && AUTHENTICATED_USERS.contains(username)) {
				return Mono.just("jai shree ram says "+username);
			}else {
				return Mono.error(() -> new IllegalStateException("user "+username+" is not authorized"));
			}
			
		});
	}
}
