package com.learning.springreactive.johnThomson;


import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

import reactor.core.publisher.Flux;

//creating main method class just to test
public class FluxMonoStreamSimiliarity {

	//uncomment to tes the scenarios
	
	//public static void main(String[] args) {
		//basicCreation();
		//basicCreationLimit();
		//basicCreationLimitSorted();
		//basicCreationLimitMap();
		//basicCreationLimitFlatMap();
		//basicCreationFilter();
		//basicCreationCollect();
	//}
	
	public static void basicCreation() {
Flux<String> itemsFlux = Flux.fromIterable(Arrays.asList("ram" , "hanuman" , "raghav" , "krishna" , "radha" , "mahadev" , "digpal"));
		
		itemsFlux.subscribe(System.out::println);
	}
	
	public static void basicCreationLimit() {
Flux<String> itemsFlux = Flux.fromIterable(Arrays.asList("ram" , "hanuman" , "raghav" , "krishna" , "radha" , "mahadev" , "digpal"));
		
		itemsFlux
		.take(4)
		.subscribe(System.out::println);
	}
	
	public static void basicCreationLimitSorted() {
		Flux<String> itemsFlux = Flux.fromIterable(Arrays.asList("ram" , "hanuman" , "raghav" , "krishna" , "radha" , "mahadev" , "digpal"));
				
				itemsFlux
				//.take(4)
				.sort()
				.subscribe(System.out::println);
			}
	
	public static void basicCreationLimitMap() {
		Flux<String> itemsFlux = Flux.fromIterable(Arrays.asList("ram" , "hanuman" , "raghav" , "krishna" , "radha" , "mahadev" , "digpal"));
				
				itemsFlux
				.take(4)
				//.sort()
				.map(String::toUpperCase)
				.subscribe(System.out::println);
			}
	
	public static void basicCreationLimitFlatMap() {
		Flux<String> itemsFlux = Flux.fromIterable(Arrays.asList("ram" , "hanuman" , "raghav" , "krishna" , "radha" , "mahadev" , "digpal"));
				
				itemsFlux
				.take(4)
				.map(item -> transformFlatMap(item))
				//.sort()
				.flatMap(Flux::just)
				.subscribe(System.out::println);
			}
	
	public static String[] transformFlatMap(String item) {
		return new String[] {item,"sita ram"};
	}
	
	public static void basicCreationFilter() {
		Flux<String> itemsFlux = Flux.fromIterable(Arrays.asList("ram" , "hanuman" , "raghav" , "krishna" , "radha" , "mahadev" , "digpal"));
				
				itemsFlux
				.filter(item -> item.length() == 3 || item.length() == 7)
				.sort()
				.subscribe(System.out::println);
			}
	
	public static void basicCreationCollect() {
		Flux<String> itemsFlux = Flux.fromIterable(Arrays.asList("ram" , "hanuman" , "raghav" , "krishna" , "radha" , "mahadev" , "digpal"));
				
				itemsFlux
				.filter(item -> item.length() == 3 || item.length() == 7)
				.sort()
				.collect(Collectors.joining(" - "))
				.subscribe(System.out::println);
			}
	
}
