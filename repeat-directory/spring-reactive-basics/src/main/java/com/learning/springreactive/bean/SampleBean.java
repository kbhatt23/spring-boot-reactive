package com.learning.springreactive.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SampleBean {

	private int id;
	
	private String name;
	
	private String description;
	
	private InnerBean innerBean;
	
}
