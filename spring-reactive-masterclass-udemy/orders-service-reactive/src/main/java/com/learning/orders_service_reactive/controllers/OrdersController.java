package com.learning.orders_service_reactive.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.orders_service_reactive.dtos.OrderDTO;
import com.learning.orders_service_reactive.service.OrderService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
public class OrdersController {

	@Autowired
	private OrderService orderService;
	
	@PostMapping
	public Mono<OrderDTO> placeOrder(@RequestBody Mono<OrderDTO> request){
		return orderService.placeOrder(request);
	}
	
	@GetMapping(path = "/users/{userID}" , produces =  MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<OrderDTO> findUserOrders(@PathVariable long userID){
		return orderService.findOrdersOfUser(userID);
	}
	
	@GetMapping("/{orderID}")
	public Mono<OrderDTO> findOrder(@PathVariable long orderID){
		return orderService.findOrder(orderID);
	}
	
	//get all users and all products , as soon as it arrives place an order
	@GetMapping(path = "/playOrders" , produces =  MediaType.TEXT_EVENT_STREAM_VALUE )
	public Flux<OrderDTO> playOrders(){
		return orderService.placeOrdersUsingAllProductsAndUsers();
	}
	
	//get all users and all products , as soon as it arrives place an order
	//bad approach when stream are of unequal size -> zip makes sure indexes are matched
		@GetMapping(path = "/playOrders2" , produces =  MediaType.TEXT_EVENT_STREAM_VALUE )
		public Flux<OrderDTO> playOrders2(){
			return orderService.placeOrdersUsingAllProductsAndUsers2();
		}
}
