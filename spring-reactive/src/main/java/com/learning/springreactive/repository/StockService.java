package com.learning.springreactive.repository;

import java.time.Duration;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.learning.springreactive.document.Stock;

import reactor.core.publisher.Flux;

@Service
public class StockService {
	Random random = new Random();

	public Flux<Stock> findAllByStockName(String name){
		
		return Flux.<Stock>generate(sink -> sink.next( new Stock(name, random.nextDouble(), new Date())))
					.delayElements(Duration.ofSeconds(1))
					.log("findAllByStockName: ")
					;
	}
}
