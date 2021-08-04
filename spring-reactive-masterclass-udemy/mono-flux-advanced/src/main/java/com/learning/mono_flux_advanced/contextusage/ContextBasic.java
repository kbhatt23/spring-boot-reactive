package com.learning.mono_flux_advanced.contextusage;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.learning.mono_flux_advanced.utils.DefaultSubscriber;

import reactor.core.publisher.Mono;
import reactor.util.context.Context;
import reactor.util.context.ContextView;

//remeber context is similar to grpc context
//in grpc client can pass data via context key value map
//simialrly here a subscriber can pass data to publisher
//remeber every operator in chain addsa processor and hence our data can be seen via upstream operator only
//downstream operator wont see one operator context data
public class ContextBasic {

	private static final String USER_NAME = "userName";
	private static final Set<String> AUTHENTICATED_USERS ;
	static {
		AUTHENTICATED_USERS = new HashSet<>(Arrays.asList("kannu","debu"));
	}
	
	public static void main(String[] args) {
		helloMessage()
		//this once overrides befor one and hence it shud give unauthenticated error
		//.contextWrite(Context.of(USER_NAME, "kaintuki"))
		
		//.contextWrite(Context.of(USER_NAME, "kannu"))
		.contextWrite(Context.of(USER_NAME, "debu"))
		.subscribe(new DefaultSubscriber<>(true, "ContextBasic"));
	}
	
	//lets say we want to add secutry
	//a subscriber can pass a token to its publisher
	//publisher/upstream can recieve it modify it and filter based on it
	private static Mono<String> helloMessage(){
		
		//return Mono.just("jai shree ram");
		
		//this is a publisher, it an get context data from its subscriber
		return Mono.deferContextual( contextView -> {
			
			String userName = contextView.get(USER_NAME);
			
			if(!StringUtils.isEmpty(userName) && AUTHENTICATED_USERS.contains(userName)) {
				return Mono.just("jai shree ram says "+userName);
			}else {
				return Mono.error( () -> new IllegalArgumentException("user "+userName+" is not authenticated"));
			}
			
		});
	}
}
