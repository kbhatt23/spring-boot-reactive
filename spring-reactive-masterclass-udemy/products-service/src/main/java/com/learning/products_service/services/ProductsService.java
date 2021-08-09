package com.learning.products_service.services;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.learning.products_service.documents.ProductDocument;
import com.learning.products_service.dtos.ProductDTO;
import com.learning.products_service.repositories.ProductsRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
@Slf4j
public class ProductsService {

	@Autowired
	private ProductsRepository productsRepository;
	
	//multiple data
	//multiple subscribers
	//hot publisher -> like shared method , for first subscriber it will be cold and for others it will be hot
	private Sinks.Many<ProductDTO> sinks ;
	
	
	//flux mean 0 or 1 or 2 or 3...or n elements
	public Flux<ProductDTO> findAll(){
		return productsRepository.findAll()
				//added just for demo
				    .delayElements(Duration.ofSeconds(1))
					.map(ProductDTO :: new)
				;
	}
	
	//mono means 0 or 1 elements
	//0 means 404
	public Mono<ResponseEntity<ProductDTO>> findByID(String id) {
		return productsRepository.findById(id)
					.map(ProductDTO :: new)
					.map(productDto -> new ResponseEntity<>(productDto, HttpStatus.OK))
					//whne none of the onnext element comes then take this
					.switchIfEmpty(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)))
					;
		        
	}
	
	//assume while creating we do not allow id to be passed
	//response dto should have the id but not the request dto
	public Mono<ResponseEntity<ProductDTO>> create(Mono<ProductDTO> requestMono){
		//start from pipeline
		return requestMono
				.doOnNext(input -> log.info("create: input requested "+input))
				.filter(request -> StringUtils.isEmpty(request.getProductId()))
				.doOnNext(input -> log.info("create: input filtered "+input))
					.map(ProductDocument :: new)
					.flatMap(productsRepository :: save)
					.map(ProductDTO :: new)
					.doOnNext(output -> log.info("create: final created data "+output))
					.doOnNext(sinks :: tryEmitNext)
					.map(saved -> new ResponseEntity<>(saved, HttpStatus.CREATED))
					.switchIfEmpty(Mono.error(() -> new IllegalArgumentException("create: can not pass id while inserting data")))
					;
			   
	}
	
	//id can not be null as it is path variable
	public Mono<ResponseEntity<ProductDTO>> update(Mono<ProductDTO> requestMono , String id){
		return 
				productsRepository.findById(id)
			// .doOnNext(current -> requestMono.map(request ->{ current.setName(request.getName()); current.setPrice(request.getPrice()); return current;}))
			 //.doOnNext(input -> log.info("update: product to insert "+input))
			 
				.flatMap(p -> requestMono.map(req -> new ProductDocument(req)))
				.flatMap(productsRepository :: save)
			 .doOnNext(output -> log.info("update: updated final product "+output))
		   .map(ProductDTO :: new)
		   .map(updated -> new ResponseEntity<>(updated, HttpStatus.OK))
		   .switchIfEmpty(Mono.error(() -> new IllegalArgumentException("update: product do not exist")))
		   ;
		
	}
	
	public Mono<ResponseEntity<Object>> delete(String id) {
		return productsRepository.deleteById(id)
				    .thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));
	}

	public Flux<ProductDTO> findByPriceRange(double minPrice, double maxPrice) {
		return productsRepository.findByPriceBetween(minPrice, maxPrice)
				 .map(ProductDTO :: new)
				;
	}
	
	public Flux<ProductDTO> findProductCreateStream() {
		//sinks object is singleton for multiple subscribers
		if(sinks == null)
			sinks= Sinks.many().replay().all();
		
		return sinks.asFlux();
	}
	
}
