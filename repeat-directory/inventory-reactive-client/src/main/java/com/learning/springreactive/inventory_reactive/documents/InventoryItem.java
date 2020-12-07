package com.learning.springreactive.inventory_reactive.documents;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItem {

	private String id;
	
	private String name;
	
	private String description;

	public InventoryItem(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	
}
