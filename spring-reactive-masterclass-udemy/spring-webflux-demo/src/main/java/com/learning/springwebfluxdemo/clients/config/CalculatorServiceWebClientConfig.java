package com.learning.springwebfluxdemo.clients.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientRequest.Builder;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.learning.springwebfluxdemo.routers.CalculatorServiceRouter;

import reactor.core.publisher.Mono;

@Configuration
public class CalculatorServiceWebClientConfig {

	public static final String ADDITION = "ADDITION";
	public static final String SUBSTRACTION = "SUBSTRACTION";
	public static final String MULTIPLICATION = "MULTIPLICATION";
	public static final String DIVISION = "DIVISION";

	@Bean
	public WebClient calculatorWebClient() {

		return WebClient.builder().baseUrl("http://localhost:9000/calculator")
				// we can not let client set this header of calculation
				// nor we can set by default/static, it will be interceptor based only
				.filter(this:: interceptCalcualtorAttribute).build();

	}

	private Mono<ClientResponse> interceptCalcualtorAttribute(ClientRequest request, ExchangeFunction next) {
		 ClientRequest requestTransformed= request.attribute("operator")
				 .map(obj -> (String) obj)
			   .map( operator -> transformClientRequest(request, operator))
			   .orElse(request)
			   ;
		 
		 return next.exchange(requestTransformed);
	}
	
	private ClientRequest transformClientRequest(ClientRequest existing, String operator ) {
		Builder from = ClientRequest.from(existing);
		
		
		switch (operator) {
		case ADDITION:
			  from.header(CalculatorServiceRouter.OPERATION, CalculatorServiceRouter.ADDITION_OPERATOR);
			break;
		case MULTIPLICATION:
			  from.header(CalculatorServiceRouter.OPERATION, CalculatorServiceRouter.MULTIPLICATION_OPERATOR);
			break;
		case DIVISION:
			  from.header(CalculatorServiceRouter.OPERATION, CalculatorServiceRouter.DIVISION_OPERATOR);
			break;
		case SUBSTRACTION:
			  from.header(CalculatorServiceRouter.OPERATION, CalculatorServiceRouter.SUBSTRACTION_OPERATOR);
			break;

		default:
			from.header(CalculatorServiceRouter.OPERATION, CalculatorServiceRouter.ADDITION_OPERATOR);
			break;
		}
		
		return from.build();
	}

}
