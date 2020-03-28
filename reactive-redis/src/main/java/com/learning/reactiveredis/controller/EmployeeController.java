package com.learning.reactiveredis.controller;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.reactiveredis.bean.Employee;
import com.learning.reactiveredis.bean.Order;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	@GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Employee> fetchAllEmployees(){
		
		return employeeService.fetchAllEmployees();
	}
	
	@PostMapping
	public Mono<Employee> createEmployee(@RequestBody Employee employee){
		return employeeService.createEmployee(employee);
	}
	
	@GetMapping("/{empId}")
	public Mono<Employee> findEmployeeById(@PathVariable("empId") String key){
		return employeeService.findEmployeeById(key);
	}
	
	@GetMapping(value = "/{empId}/orders" , produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Order> findEmployeeOrders(@PathVariable("empId") String key){
		return employeeService.findEmployeeOrders(key)
					.delayElements(Duration.ofSeconds(1))
				;
	}
	
}
