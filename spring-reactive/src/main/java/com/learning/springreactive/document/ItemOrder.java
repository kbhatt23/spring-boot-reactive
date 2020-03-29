package com.learning.springreactive.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemOrder {

	private String orderId;
	
	private String name;
	
	private Integer itemCount;
}
