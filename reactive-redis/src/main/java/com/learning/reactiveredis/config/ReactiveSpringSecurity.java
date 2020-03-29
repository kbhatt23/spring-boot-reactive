package com.learning.reactiveredis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class ReactiveSpringSecurity {

	@Bean
	public MapReactiveUserDetailsService authentication() {
		UserDetails userDetails1  = User.withDefaultPasswordEncoder()
			.username("kbhatt23")
			.password("jaishreeram")
			.roles("USER")
			.build();
		
		UserDetails userDetailsAdmin  = User.withDefaultPasswordEncoder()
				.username("sitaRam")
				.password("ramadutahanuman")
				.roles("ADMIN")
				.build();
		
		return new MapReactiveUserDetailsService(userDetails1,userDetailsAdmin);
	}
	
	@Bean
	public SecurityWebFilterChain authorize(ServerHttpSecurity http) {
		return http.httpBasic()
			.and()
			.authorizeExchange()
			.anyExchange()
			.hasRole("ADMIN")
			.and()
			.build();
		
	}
}
