package com.learning.springwebfluxdemo.routers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.learning.springwebfluxdemo.handlers.CalculatorService;

@Configuration
public class CalculatorServiceRouter {

	public static final String OPERATION = "operation";

	public static final String ADDITION_OPERATOR = "+";
	
	public static final String SUBSTRACTION_OPERATOR = "-";
	
	public static final String MULTIPLICATION_OPERATOR = "*";
	
	public static final String DIVISION_OPERATOR = "/";
	
	@Autowired
	private CalculatorService calculatorService;
	
	@Bean
	public RouterFunction<ServerResponse> routeCalculatorService(){
		
	return	RouterFunctions.route(RequestPredicates.GET("/calculator/{num1}/{num2}")
				.and(RequestPredicates.headers(header -> ADDITION_OPERATOR.equals(header.firstHeader(OPERATION))))
				, calculatorService :: addition)
			  .andRoute(RequestPredicates.GET("/calculator/{num1}/{num2}")
				.and(RequestPredicates.headers(header -> SUBSTRACTION_OPERATOR.equals(header.firstHeader(OPERATION))))
				, calculatorService :: substraction)
			  .andRoute(RequestPredicates.GET("/calculator/{num1}/{num2}")
						.and(RequestPredicates.headers(header -> MULTIPLICATION_OPERATOR.equals(header.firstHeader(OPERATION))))
						, calculatorService :: multiplication)
			  .andRoute(RequestPredicates.GET("/calculator/{num1}/{num2}")
						.and(RequestPredicates.headers(header -> DIVISION_OPERATOR.equals(header.firstHeader(OPERATION))))
						, calculatorService :: division)
			
		;
		
	}
}
