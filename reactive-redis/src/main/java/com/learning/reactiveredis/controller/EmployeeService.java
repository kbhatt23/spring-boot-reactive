package com.learning.reactiveredis.controller;

import java.time.Duration;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.learning.reactiveredis.bean.Employee;
import com.learning.reactiveredis.bean.Order;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EmployeeService {

	@Autowired
	private ReactiveRedisTemplate<String, Employee> employeeTemplate;
	
	public Flux<Employee> fetchAllEmployees(){
		
		return employeeTemplate.keys("*")//return flux<string> as key is tring for emplyeetmplate
				.delayElements(Duration.ofSeconds(1)) // fake delay just to see the stream		
				.flatMap(key -> employeeTemplate.opsForValue().get(key));
	}
	
	public Mono<Employee> createEmployee( Employee employee){
		return employeeTemplate.opsForValue().set(employee.getAge(), employee)
			.map(result -> employee)
		;
	}
	
	public Mono<Employee> findEmployeeById( String key){
		return employeeTemplate.opsForValue().get(key);
	}
	
	public Flux<Order> findEmployeeOrders(@PathVariable("empId") String key){
		return employeeTemplate.opsForValue().get(key)
									  .delayElement(Duration.ofSeconds(1))
									  .flatMapMany(employee -> {
										  return Flux.generate(sink -> sink.next(new Order(employee.getName(), new Date())));
									  })
									  
									  ;
						
	}
}
