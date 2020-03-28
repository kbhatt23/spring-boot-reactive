package com.learning.reactiveredis;

import java.util.Random;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;

import com.learning.reactiveredis.bean.Employee;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class ReactiveRedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveRedisApplication.class, args);
	}
	
	

}
@Component
class DataLoader{
	
	@Autowired
	private ReactiveRedisTemplate<String, Employee> employeeRedisTemplete;
	
	@Autowired
	ReactiveRedisConnectionFactory factory;
	@PostConstruct
	public void saveDataInRedis() { 
		factory.getReactiveConnection().serverCommands().flushAll()//returns mono<string>
		//need to use then many as we need to take inpout as mono but output shud be flux
		.thenMany(Flux.just("radha krishna", "uma maheshwar" , "sita ram" , "pavanputra hanuman"))//output is flux<String>
			.map(name -> new Employee(name, UUID.randomUUID().toString())) // output is flux<Employee>
			.flatMap(employee -> employeeRedisTemplete.opsForValue().set(employee.getAge(), employee))
			.log()
			.subscribe(entry -> System.out.println("Adding entry in redis for employee "+entry))
			;
	}
}
