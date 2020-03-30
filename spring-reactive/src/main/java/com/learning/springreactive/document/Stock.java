package com.learning.springreactive.document;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//not using mongo db as of now
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {

	private String name;
	
	private Double price;
	
	private Date date;
}
