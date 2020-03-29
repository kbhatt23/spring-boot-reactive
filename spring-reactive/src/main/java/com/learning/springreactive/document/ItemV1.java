package com.learning.springreactive.document;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemV1 {

	@Id
	private String id;
	private String descriprion;
	private Double price;
	private List<ItemOrder> orders;
}
