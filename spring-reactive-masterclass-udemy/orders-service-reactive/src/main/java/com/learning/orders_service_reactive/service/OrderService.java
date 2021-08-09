package com.learning.orders_service_reactive.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.learning.orders_service_reactive.dtos.OrderDTO;
import com.learning.orders_service_reactive.dtos.OrderRespository;
import com.learning.orders_service_reactive.dtos.ProductDTO;
import com.learning.orders_service_reactive.dtos.TransactionType;
import com.learning.orders_service_reactive.dtos.UserDTO;
import com.learning.orders_service_reactive.dtos.UserTransactionDTO;
import com.learning.orders_service_reactive.entities.OrderEntity;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class OrderService {

	@Autowired
	private UserClientService userClientService;
	
	@Autowired
	private ProductClientService productClientService;
	
	@Autowired
	private OrderRespository orderRespository;
	
	public Mono<OrderDTO> placeOrder(Mono<OrderDTO> request){
		
		 return request.filter(order -> StringUtils.isEmpty(order.getId())) // order request should not have id
				      .doOnNext(order -> log.info("placeOrder: first filter passed"))
				      .filter(order -> !StringUtils.isEmpty(order.getUserID()) && !StringUtils.isEmpty(order.getProductID())) // order request should have product and user ids
				      .doOnNext(order -> log.info("placeOrder: second filter passed"))
				      .flatMap(this:: updateProductPrice)
				      .doOnNext(order -> log.info("placeOrder: order with updated price "+order))
				      .flatMap(this :: debitUserAccount)
				      .map(OrderEntity :: new)
				      .flatMap(orderRespository :: save)
				      .map(OrderDTO:: new)
				      .switchIfEmpty(Mono.error(() -> new IllegalArgumentException("placeOrder: can not place order")))
				      .onErrorResume(this :: handleError)
				      ;
		 
	}
	
	private Mono<OrderDTO> updateProductPrice(OrderDTO orderDTO){
			return productClientService.findProduct(orderDTO.getProductID())
					.map(product -> {
						orderDTO.setPrice(product.getPrice());
						return orderDTO;
					})
			;
	}
	private Mono<OrderDTO> debitUserAccount(OrderDTO orderDTO){
		return userClientService.debitAmount(Mono.just(new UserTransactionDTO(null, orderDTO.getPrice(), orderDTO.getUserID(), TransactionType.DEBIT, LocalDateTime.now())))
						.map(transaction -> {
							orderDTO.setTransactionId(transaction.getId());
							return orderDTO;
						});
}
	
	public Flux<OrderDTO> findOrdersOfUser(long userID){
		return orderRespository.findByUserID(userID)
				.map(OrderDTO:: new)
				;
	}
	public Mono<OrderDTO> findOrder(long orderID){
		return orderRespository.findById(orderID)
				.map(OrderDTO:: new)
				;
	}
	
	private Mono<OrderDTO> handleError(Throwable error) {
		log.error("handleError: error occurred "+error.getMessage());;
		return Mono.error(() -> new IllegalArgumentException("placeOrder: can not place order"));
	}
	
	
	//find all the users in strea, format
	//find all products zip them with latest and place the order
	public Flux<OrderDTO> placeOrdersUsingAllProductsAndUsers() {
		
		Flux<UserDTO> allUsersFlux = userClientService.findAllUsers();
		
		Flux<ProductDTO> allProductsFlux = productClientService.findAllProducts();
		
		return Flux.combineLatest(items -> {
			UserDTO user = (UserDTO) items[0];
			ProductDTO product = (ProductDTO) items[1];
			return  Mono.just(new OrderDTO(user.getId(), product.getProductId()));
		}, allUsersFlux,allProductsFlux)
		//get order DTO one by one
		.flatMap(this :: placeOrder)
		;
	}
	
	//using zip is bad as if one of the stream finishes , then other stream keeps onw aiting 
	//combine latest fixes this issue
	public Flux<OrderDTO> placeOrdersUsingAllProductsAndUsers2() {
		
		Flux<UserDTO> allUsersFlux = userClientService.findAllUsers();
		
		Flux<ProductDTO> allProductsFlux = productClientService.findAllProducts();
		
		return Flux.zip(items -> {
			UserDTO user = (UserDTO) items[0];
			ProductDTO product = (ProductDTO) items[1];
			return  Mono.just(new OrderDTO(user.getId(), product.getProductId()));
		}, allUsersFlux,allProductsFlux)
		//get order DTO one by one
		.flatMap(this :: placeOrder)
		;
	}
	
}
