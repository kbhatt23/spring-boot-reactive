package com.learning.springreactive.inventory_reactive.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItem {

	//automatically in mongo db id is string and autogeenrated 
	//if we do not put @id for any property/field than one id field will be extra in the D.B
	@Id
	private String id;
	
	private String name;
	
	private String description;

	public InventoryItem(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	
}
