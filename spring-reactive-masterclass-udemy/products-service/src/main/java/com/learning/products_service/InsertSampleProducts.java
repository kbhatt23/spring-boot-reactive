package com.learning.products_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import com.learning.products_service.documents.ProductDocument;
import com.learning.products_service.repositories.ProductsRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class InsertSampleProducts implements CommandLineRunner{

	@Autowired
	private ProductsRepository productsRepository;
	
	@Override
	public void run(String... args) throws Exception {
		log.info("run: Inserting products in Mongo DB");
		
		productsRepository.deleteAll()
						  .doOnSuccess(voidData -> log.info("run: Removed all old products"))
		                  .thenMany(Flux.range(1, 5))//insert 5 documents
		                  .map(i -> new ProductDocument("product-"+i, "product name "+i, i * 10d))
		                  .flatMap(productsRepository :: save)
		                  .doOnNext(saved -> log.info("run: Saved product "+saved))
		                  .doOnComplete(() -> log.info("run: Inserted 5 products in Mongo DB"))
		                  .blockLast()
		                  ;
	}

}
