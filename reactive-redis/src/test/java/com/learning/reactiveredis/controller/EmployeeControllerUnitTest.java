package com.learning.reactiveredis.controller;

import java.time.Duration;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;


import com.learning.reactiveredis.ReactiveRedisApplication;
import com.learning.reactiveredis.bean.Employee;
import com.learning.reactiveredis.bean.Order;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
//@SpringBootTest
//we can use webfluxtest below instead
//@EnableWebFlux
@WebFluxTest
@ContextConfiguration(classes = ReactiveRedisApplication.class)
public class EmployeeControllerUnitTest {

	@MockBean
	private EmployeeService employeeService;
	
	@Autowired
	private EmployeeController employeeController;
	
	
	private Employee employee;
	
	//@Autowired
	private WebTestClient webtestCleint;
	
	@Autowired
    ApplicationContext context;
	
	@Before
	public void mockitoSetup() {
		this.webtestCleint = WebTestClient.bindToApplicationContext(this.context).configureClient().build();
		
		employee = new Employee("fake employee", "fake age");
		Mockito.when(employeeService.findEmployeeById(employee.getAge()))
				.thenReturn(Mono.just(employee));
		
		Mockito.when(employeeService.findEmployeeOrders(employee.getAge()))
				.thenReturn(Flux.<Order>generate(sink -> sink.next(new Order(employee.getName(), new Date())))
						//forced delay
						.delayElements(Duration.ofSeconds(1))
						.take(10))
				;
		
		Mockito.when(employeeService.fetchAllEmployees())
				.thenReturn(Flux.just(employee,employee,employee).delayElements(Duration.ofSeconds(1)));
	}
	
	@Test
	public void testFindAllAPIURL() {
		System.out.println("started test testFindAllAPIURL");
		long start = System.currentTimeMillis();
		Flux<Employee> allFlux = webtestCleint.get()
					 .uri("/employees")
					 .accept(MediaType.APPLICATION_STREAM_JSON)
					 .exchange()
					 .returnResult(Employee.class)
					 .getResponseBody()
					 ;
		
		StepVerifier.create(allFlux)
					.thenAwait(Duration.ofSeconds(3))
					.expectNext(employee,employee,employee)
					.verifyComplete();
		long end = System.currentTimeMillis();
		double totatSeconds = (end-start)/1000;
		System.out.println("completed test testFindAllAPIURL in time "+totatSeconds);
	}
	
	@Test
	public void testFindById() {
		System.out.println("started test testFindById");
		Mono<Employee> empFound = employeeController.findEmployeeById(employee.getAge());
		StepVerifier.create(empFound)
					.expectSubscription()
					.expectNext(employee)
					.verifyComplete()
					;
		
		System.out.println("completed test testFindById");
	}
	
	//this forcefully takes 10 second as each item has delay of 1 second
	@Test
	public void testFindEmployeeOrders() {
		System.out.println("started test testFindEmployeeOrders");
		long start = System.currentTimeMillis();
		Flux<Order> empFoundOrders = employeeController.findEmployeeOrders(employee.getAge()).take(10);
		StepVerifier.create(empFoundOrders)
					.expectSubscription()
					.expectNextCount(10)
					.verifyComplete();
		long end = System.currentTimeMillis();
		double totatSeconds = (end-start)/1000;
		System.out.println("completed test testFindEmployeeOrders in time "+totatSeconds);
	}
	
	//below will take less time
	//@Test
	public void testFindEmployeeOrdersWithTimer() {
		System.out.println("started test testFindEmployeeOrdersWithTimer");
		long start = System.currentTimeMillis();
		Flux<Order> empFoundOrders = employeeController.findEmployeeOrders(employee.getAge()).take(10);
		
		StepVerifier.withVirtualTime(() -> empFoundOrders)
					.expectSubscription()
					.thenAwait(Duration.ofHours(10))
					.expectNextCount(10)
					.verifyComplete();
		long end = System.currentTimeMillis();
		double totatSeconds = (end-start)/1000;
		System.out.println("completed test testFindEmployeeOrdersWithTimer in time "+totatSeconds);
	}
	
	@Test
	public void testFindAllEmployees() {
		System.out.println("started test testFindAllEmployees");
		StepVerifier.create( employeeController.fetchAllEmployees())
		.expectSubscription()
		.thenAwait(Duration.ofSeconds(3))
					.expectNext(employee,employee,employee)
					.verifyComplete();
		
		
		
		System.out.println("completed test testFindAllEmployees");
		
	}
}
