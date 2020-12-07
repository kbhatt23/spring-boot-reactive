package com.learning.springreactive.inventory_reactive;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import com.learning.springreactive.inventory_reactive.documents.InventoryItem;
import com.learning.springreactive.inventory_reactive.documents.InventoryItemCapped;
import com.learning.springreactive.inventory_reactive.repositories.InventoryItemCappedRepository;
import com.learning.springreactive.inventory_reactive.repositories.InventoryItemRepository;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class InventoryReactiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryReactiveApplication.class, args);
	}
	
	

}
@Component
class InitializeData implements CommandLineRunner{

	@Autowired
	private InventoryItemRepository repo;
	
	@Autowired
	private InventoryItemCappedRepository repoCapped;
	
	@Autowired
	private MongoOperations mongoOperations;
	
	private List<InventoryItem> items = Arrays.asList(new InventoryItem("sita-ram", "jai sita ram"),
			new InventoryItem("radhe-krishna", "jai radhe krishna"),
			new InventoryItem("ABC","radhe-krishna", "jai radhe krishna")//this time id will not be auto generated
			);
	
	@Override
	public void run(String... args) throws Exception {
		
		
		repo.deleteAll()
			.thenMany(Flux.fromIterable(items))
			.flatMap(repo::save)
			.doOnNext(item -> System.out.println("InitializeData: Created inventory item "+item))
			.blockLast()
			;
		
		createCappedCollection();
		startPushingDataToCappedCollection();
	}

	private void startPushingDataToCappedCollection() {

		Flux.interval(Duration.ofSeconds(1))
			.map(i -> new InventoryItemCapped("live stream "+i, "jai shree ram says "+i))
			.flatMap(repoCapped::save)
			.subscribe(item -> System.out.println("Added Capped item "+item))
			;
		
	}

	private void createCappedCollection() {
		mongoOperations.dropCollection(InventoryItemCapped.class);
		mongoOperations.createCollection(InventoryItemCapped.class, CollectionOptions.empty().maxDocuments(10).size(50000).capped());
	}
	
}
