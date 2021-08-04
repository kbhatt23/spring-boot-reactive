package com.learning.mono_basics.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.github.javafaker.Faker;

import reactor.core.publisher.Mono;

public class MonoStreamsUtils {

	public static void printOnNext(Object message) {
		System.out.println("message recieved " + message);
	}

	public static void printOnError(Throwable error) {
		System.out.println("error occurred " + error);
	}

	public static void printOnComplete() {
		System.out.println("all tasks done");
	}

	public static final Faker FAKER = Faker.instance();

	public static Mono<String> findUser(int id) {

		switch (id) {
		case 1:
			return Mono.just(FAKER.name().firstName());
		case 2:
			return Mono.empty();
		default:
			return Mono.error(() -> new IllegalArgumentException("user id " + id + " do not exist"));
		}
	}

	public static String generateName() {
		System.out.println("generateName called");
		//time consuming task
		ThreadUtils.sleep(1000);
		
		return FAKER.name().firstName();
	}
	
	public static List<String> generateNames(int count){
		System.out.println("generateNames called");
		return IntStream.rangeClosed(1, count)
				 .mapToObj(i -> generateName())
				 .collect(Collectors.toList());
	}
}
