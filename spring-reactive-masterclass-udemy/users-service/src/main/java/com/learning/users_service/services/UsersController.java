package com.learning.users_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.users_service.dtos.UserDTO;
import com.learning.users_service.dtos.UserTransactionDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UsersController {

	@Autowired
	private UserService userService;

	@GetMapping
	public Flux<UserDTO> findAll() {
		return userService.findAllUsers();
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<UserDTO>> findByID(@PathVariable long id) {
		return userService.findByID(id);
	}

	@PostMapping
	public Mono<ResponseEntity<UserDTO>> createUser(@RequestBody Mono<UserDTO> request) {
		return userService.create(request);
	}


	@PutMapping("/{id}")
	public Mono<ResponseEntity<UserDTO>> update(@RequestBody Mono<UserDTO> requestMono, @PathVariable long id) {
		return userService.update(requestMono, id);
	}

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Object>> delete(@PathVariable long id) {
		return userService.delete(id);
	}
	
	@PostMapping("/credit")
	public Mono<ResponseEntity<UserTransactionDTO>> credit(@RequestBody Mono<UserTransactionDTO> request){
		return request.flatMap(userService :: credit);
	}

	
	@PostMapping("/debit")
	public Mono<ResponseEntity<UserTransactionDTO>> debit(@RequestBody Mono<UserTransactionDTO> request){
		return request.flatMap(userService :: debit);
	}
	
	@GetMapping("/transactions")
	public Flux<UserTransactionDTO> findAllTransactionsOfAllUsers(){
		return userService.findAllTransactionsOfAllUsers();
	}
	
	@GetMapping("/{userId}/transactions")
	public Flux<UserTransactionDTO> findAllTransactionsOfSpecificUser(@PathVariable long userId){
		return userService.findAllTransactionsOfSpecificUser(userId);
	}

}
