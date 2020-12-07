package com.learning.springreactive.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public  class InnerBean{
	private String innerProperty;
	
	private String innerDescription;
}