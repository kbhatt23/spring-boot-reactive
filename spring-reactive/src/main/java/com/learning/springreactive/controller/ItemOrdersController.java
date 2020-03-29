package com.learning.springreactive.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.springreactive.document.ItemOrder;
import com.learning.springreactive.document.ItemV1;
import com.learning.springreactive.repository.ItemV1ReactiveRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/itemOrders")
public class ItemOrdersController {

	@Autowired
	ItemV1ReactiveRepository ordersRepository ;
	@GetMapping
	public Flux<ItemV1> fetchAllOrdersById() {
		System.out.println("jai shree ram for orders repository");
		return ordersRepository.findAll()
				.log()
				;
	}
	
	@GetMapping("/test")
	public String testOrders() {
		return "jai shree ram";
	}
	
	@GetMapping("/{id}")
	public Mono<List<ItemOrder>> fetchAllOrdersByIdPAthVariable(@PathVariable String id) {
		System.out.println("fetchAllOrdersByIdPAthVariable: jai shree ram for orders repository");
		return ordersRepository.findById(id)
				.log()
				.map(ItemV1::getOrders)
				;
	}
}
