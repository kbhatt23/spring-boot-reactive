package com.learning.springwebfluxdemo.services;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.learning.springwebfluxdemo.dtos.MathResponseDTO;
import com.learning.springwebfluxdemo.dtos.MultiplyRequest;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Service
@Slf4j
public class MathsWebFluxService {

	public double squareRoot(int number) {
		return Math.sqrt(number);
	}
	
	public Mono<MathResponseDTO> squareRootLazy(int number) {
		//make it as lazy as possible
		//start calculation only when subscriber subscribes
		return Mono.create(monoSink -> {
			monoSink.success(new MathResponseDTO(squareRoot(number)));
		});
	}
	
	public List<Integer> numberTable(int number) {
		
		return IntStream.rangeClosed(1, 10)
						.map(i -> i *number)
						.boxed()
						.collect(Collectors.toList());
	}

	public Flux<MathResponseDTO> numberTableLazy(int number) {
		//lazy until subscriber subscribes do nothing
		//also cancel if subcriber cancels
//		return Flux.create(fluxSink -> {
//			IntStream.rangeClosed(1, 10)
//			  		 .filter(i -> !fluxSink.isCancelled())
//			  		 .peek(i -> ThreadUtils.sleep(1000)) // peek is a consumer and terminal oeprator
//			  		.peek(i -> log.info("numberTableLazy: Generating table for number "+number))// peek is a consumer and terminal oeprator
//			  		 .mapToObj(i ->  new MathResponseDTO(i*number))
//			  		 .forEach(obj -> fluxSink.next(obj))
//			  		 ;
//			fluxSink.complete();
//		})
//				.subscribeOn(Schedulers.boundedElastic())
//				.cast(MathResponseDTO.class)
//				;
				;
				
			//for OOB flux method we need not to check if sink is cancelled
				//only for flux.create method we need to do it
		return Flux.range(1, 10)
				.delayElements(Duration.ofSeconds(1))
				//.doOnNext(i -> ThreadUtils.sleep(1000)) // peek is a consumer and terminal oeprator
				.doOnNext(i -> log.info("numberTableLazy: generating table for number "+number) )
				.map(i ->  new MathResponseDTO(i*number))
				;
				
	}
	
	public Flux<Integer> numberTableEager(int number) {

		return Flux.fromIterable(numberTable(number));
	}

	public Mono<MathResponseDTO> multipleLazily(Mono<MultiplyRequest> request) {

		return request.map(req -> req.getNumber1() * req.getNumber2())
					  .map(res -> new MathResponseDTO(res))
					  ;
	}
}
