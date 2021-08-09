package com.learning.orders_service_reactive.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.learning.orders_service_reactive.dtos.TransactionType;
import com.learning.orders_service_reactive.dtos.UserTransactionDTO;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class UserClientServiceTest {

	@Autowired
	private UserClientService userClientService;
	@Test
	void testDebitAmount() {
		long userID = 1l;
		UserTransactionDTO userTransactionDTO = new UserTransactionDTO(null, 10d, userID, TransactionType.DEBIT, null);
		StepVerifier.create(userClientService.debitAmount(Mono.just(userTransactionDTO)))
					.expectSubscription()
					.assertNext(res -> res.getUserID().equals(userID))
					.verifyComplete()
		;
	}

	@Test
	void testFindUser() {
		long userID = 1;
		StepVerifier.create(userClientService.findUser(userID))
		   .expectSubscription()
		   .assertNext(res -> res.getId().equals(userID))
		   .verifyComplete();
		  
	}

}
