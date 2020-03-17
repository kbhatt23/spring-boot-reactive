package com.learning.springreactive;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import com.learning.springreactive.document.CappedItem;
import com.learning.springreactive.document.Item;
import com.learning.springreactive.repository.CappedItemReactiveRepository;
import com.learning.springreactive.repository.ItemReactiveRepository;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringReactiveApplication {

	//mongo config component
	@Autowired
	private MongoOperations mongoOperations;
	
	@Autowired
	ItemReactiveRepository itemReactiveRepository;
	
	@Autowired
	CappedItemReactiveRepository cappedItemReactiveRepository;
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
			
			createCappedCollection();
			createCappedCollectionData();
		};
	}

	private void createCappedCollectionData() {

		System.out.println("createCappedCollectionData: Creating capped ocllection data with name CappedItem");
		
		//infinite stream data getting inserted into the DB
		//hence not blocking it
		//a capped collection nevert kills connection
		//thats why non blocking codeis still waiting
		Flux.interval(Duration.ofSeconds(1))//returns<Flux<Long>>
			.map(i -> new CappedItem(null, "sample capped collection "+i, 100.01+i))//returns<Flux<CappedItem>>
			.flatMap(cappedEntry -> cappedItemReactiveRepository.save(cappedEntry))//in map it will return Flux<Mono>
			//with flatmpa it requtns FLux<CappedItem>
			.subscribe(savedEntry -> System.out.println("createCappedCollectionData: capped collection data saved "+savedEntry)
					, error -> System.out.println("createCappedCollectionData : error occured while inserting capped collection "+error.getMessage())
					,() -> System.out.println("createCappedCollectionData : All insertion done for capped collection")
					)
			
			
			;
		
		
	}

	//here we are fixing size of capped collection
	//once max is reached it removes oldest entry
	//it always preserve the inserton order like queue
	private void createCappedCollection() {
		System.out.println("createCappedCollection: Creating capped ocllection with name CappedItem");
		mongoOperations.dropCollection(CappedItem.class);
		//creating empty capped collection with max document of 20 and each document can contain 20000 size of data
		mongoOperations.createCollection(CappedItem.class,CollectionOptions.empty().maxDocuments(20).size(20000).capped() );
	}
}
