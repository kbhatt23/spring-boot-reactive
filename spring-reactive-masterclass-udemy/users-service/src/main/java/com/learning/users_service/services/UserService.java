package com.learning.users_service.services;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.learning.users_service.dtos.UserDTO;
import com.learning.users_service.dtos.UserTransactionDTO;
import com.learning.users_service.entities.TransactionType;
import com.learning.users_service.entities.UserEntity;
import com.learning.users_service.entities.UserTransactionEntity;
import com.learning.users_service.repositories.UserTransactionRepository;
import com.learning.users_service.repositories.UsersRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserService {

	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private UserTransactionRepository userTransactionRepository;
	
	public Flux<UserDTO> findAllUsers(){
		return usersRepository.findAll()
				.delayElements(Duration.ofSeconds(1))
				.map(UserDTO :: new)
				;
	}
	
	//mono means 0 or 1 elements
	//0 means 404
	public Mono<ResponseEntity<UserDTO>> findByID(long id) {
		return usersRepository.findById(id)
					.map(UserDTO :: new)
					.map(productDto -> new ResponseEntity<>(productDto, HttpStatus.OK))
					//whne none of the onnext element comes then take this
					.switchIfEmpty(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)))
					;
		        
	}
	
	public Mono<ResponseEntity<UserDTO>> create(Mono<UserDTO> requestMono) {

		//start from pipeline
		return requestMono
				.doOnNext(input -> log.info("create: input requested "+input))
				.filter(request -> StringUtils.isEmpty(request.getId()))
				.doOnNext(input -> log.info("create: input filtered "+input))
					.map(UserEntity :: new)
					.doOnNext(input -> log.info("create: first transformation "+input))
					.flatMap(usersRepository :: save)
					.map(UserDTO :: new)
					.doOnNext(output -> log.info("create: final created data "+output))
					.map(saved -> new ResponseEntity<>(saved, HttpStatus.CREATED))
					.switchIfEmpty(Mono.error(() -> new IllegalArgumentException("create: can not pass id while inserting data")))
					;
			   
	
	}
	

	
	//id can not be null as it is path variable
	public Mono<ResponseEntity<UserDTO>> update(Mono<UserDTO> requestMono , long id){
		return 
				usersRepository.findById(id)
			// .doOnNext(current -> requestMono.map(request ->{ current.setName(request.getName()); current.setPrice(request.getPrice()); return current;}))
			 //.doOnNext(input -> log.info("update: product to insert "+input))
			 
				.flatMap(p -> requestMono.map(req -> {
					 UserEntity userEntity = new UserEntity(req);
					 userEntity.setId(id);
					 return userEntity;
				}))
				.flatMap(usersRepository :: save)
			 .doOnNext(output -> log.info("update: updated final product "+output))
		   .map(UserDTO :: new)
		   .map(updated -> new ResponseEntity<>(updated, HttpStatus.OK))
		   .switchIfEmpty(Mono.error(() -> new IllegalArgumentException("update: user do not exist")))
		   ;
		
	}
	
	public Mono<ResponseEntity<Object>> delete(long id) {
		return usersRepository.deleteById(id)
				    .thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));
	}
	
	public Mono<ResponseEntity<UserTransactionDTO>> credit(UserTransactionDTO requestDto) {
		
		   return this.usersRepository.creditUserBalance(requestDto.getUserID(), requestDto.getAmount())
                   .filter(i -> i == true)
                   	.doOnNext(c -> System.out.println("first filter passed"))
                   .map(b -> new UserTransactionEntity(requestDto))
                   .doOnNext(validated -> validated.setTransactionType(TransactionType.CREDIT))
                   .flatMap(this.userTransactionRepository::save)
                   .map(UserTransactionDTO :: new)
                   .map(i -> new ResponseEntity<>(i, HttpStatus.OK))
            	   .switchIfEmpty(Mono.error(() -> new IllegalArgumentException("debit: unable to process credit")));
		
		
		
	}
	
	public Mono<ResponseEntity<UserTransactionDTO>> debit(UserTransactionDTO requestDto) {

		   return this.usersRepository.debitUserBalance(requestDto.getUserID(), requestDto.getAmount())
                .filter(i -> i == true)
                	.doOnNext(c -> System.out.println("first filter passed"))
                .map(b -> new UserTransactionEntity(requestDto))
                .doOnNext(validated -> validated.setTransactionType(TransactionType.DEBIT))
                .flatMap(this.userTransactionRepository::save)
                .map(UserTransactionDTO :: new)
                .map(i -> new ResponseEntity<>(i, HttpStatus.OK))
         	   .switchIfEmpty(Mono.error(() -> new IllegalArgumentException("debit: unable to process debit")));
		
	
	}

	public Flux<UserTransactionDTO> findAllTransactionsOfAllUsers() {
		return userTransactionRepository.findAll().map(UserTransactionDTO :: new);
	}

	public Flux<UserTransactionDTO> findAllTransactionsOfSpecificUser(long userId) {
		return userTransactionRepository.findByUserID(userId).map(UserTransactionDTO :: new);
	}
}
