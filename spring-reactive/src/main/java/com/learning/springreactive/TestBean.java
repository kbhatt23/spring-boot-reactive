package com.learning.springreactive;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TestBean {

	private String name;
	
	private int age;
	
}
