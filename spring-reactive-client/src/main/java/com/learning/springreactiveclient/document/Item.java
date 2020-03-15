package com.learning.springreactiveclient.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
	private String id;
	private String descriprion;
	private Double price;

}
