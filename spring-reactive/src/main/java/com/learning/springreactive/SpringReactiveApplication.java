package com.learning.springreactive;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import com.learning.springreactive.document.Item;
import com.learning.springreactive.repository.ItemReactiveRepository;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringReactiveApplication {

	@Autowired
	ItemReactiveRepository itemReactiveRepository;
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SpringReactiveApplication.class, args);
		TestBean bean = context.getBean(TestBean.class);
		System.out.println(new TestBean("messi",23));
		bean.setAge(32);
		System.out.println(bean);
		System.out.println("creating Mongo Data for Item repistory");
		
	
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return (args) -> {
			ItemReactiveRepository repostiory = itemReactiveRepository;
			
			//cleaning up old data and putting new data , as the DB data should not be persisted after restart
			List<Item> itemsList = Arrays.asList(new Item(null, "samsung tv", 199.0)
					,new Item(null, "lg tv", 201.0),
					new Item(null, "lenovo laptop", 189.0)
					,new Item("fake", "fake device", 189.0)
					);
			
			repostiory.deleteAll()
					  .thenMany(Flux.fromIterable(itemsList)) // returns Flux<Flux<Item>>
					  //hence using flatmap
					  .flatMap(repostiory::save)
					  //.doOnNext(item -> System.out.println("SpringReactiveApplication: adding item to DB " +item))
					  //.blockLast()
					  .log()
					  .subscribe(item -> System.out.println("SpringReactiveApplication: adding item to DB " +item))
					  ;
		};
	}
}
