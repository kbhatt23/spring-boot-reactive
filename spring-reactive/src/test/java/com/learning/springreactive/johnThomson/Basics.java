package com.learning.springreactive.johnThomson;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.learning.springreactive.document.Person;
import com.learning.springreactive.dto.PersonDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class Basics {

	@Test
	public void basicMono() {
		Person person = new Person("sita ram", -1, -1);
		//Person worngPerson = new Person("radha krishna", -1, -1);
		Mono<Person> monoPerson = Mono.just(person);
		Person person1 = monoPerson.block();
		assertEquals(person, person1);
	}
	
	@Test
	public void basicMonoTransformation() {
		
		Person person = new Person("sita ram", -1, -1);
		//Person worngPerson = new Person("radha krishna", -1, -1);
		PersonDTO expectDTO = new PersonDTO(person.getName(), person.getAge());
		Mono<Person> monoPerson = Mono.just(person);
		PersonDTO person1 = monoPerson
							.map(p -> new PersonDTO(p.getName(), p.getAge()))
							.block();
		assertEquals(person.getAge(), person1.getAge());
		assertEquals(person.getName(), person.getName());
		assertEquals(expectDTO, person1);
	}
	
	@Test
	public void basicFluxTransformationDelay() {
		Person person1 = new Person("sita ram", -1, -1);
		Person person2 = new Person("radha krishna", -1, -1);
		Flux<Person> fluxPerson = Flux.just(person1,person2).delayElements(Duration.ofSeconds(1));
		
		
		fluxPerson
		    .subscribe(entry -> System.out.println("extry found "+entry));
		
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void basicFluxTransformationDefaultDelay() {
		Person person1 = new Person("sita ram", -1, -1);
		Person person2 = new Person("radha krishna", -1, -1);
		Flux<Person> fluxPerson = Flux.just(person1,person2)
										.filter(personItem -> personItem.getName().equals("pavan putra"))
										.defaultIfEmpty(new Person("uma maheshwar", 0, 0))
									;
		
		
		fluxPerson
		    .subscribe(entry -> System.out.println("basicFluxTransformationDefaultDelay: entry found "+entry))
		    ;
	}
}
