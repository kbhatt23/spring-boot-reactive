package com.learning.orders_service_reactive.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.test.StepVerifier;

@SpringBootTest
class ProductClientServiceTest {

	@Autowired
	private ProductClientService productClientService;
	
	@Test
	public void findProductTest() {
		String productID = "product-1";
		StepVerifier.create(productClientService.findProduct(productID))
				.expectSubscription()
				.expectNextMatches(dto -> dto.getProductId().equals(productID))
				.verifyComplete();
		
	}

}
