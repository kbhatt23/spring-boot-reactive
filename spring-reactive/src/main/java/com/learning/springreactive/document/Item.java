package com.learning.springreactive.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@NoArgsConstructor
@AllArgsConstructor
//the below includes toString,hanshcode,equals methods, setter and getters
@Data
public class Item {
	@Id
	private String id;
	private String descriprion;
	private Double price;

}
