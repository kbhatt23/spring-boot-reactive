package com.learning.products_service.dtos;

import com.learning.products_service.documents.ProductDocument;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
	private String productId;

	private String name;
	
	private double price;
	
	public ProductDTO(ProductDocument productDocument) {
		this.productId=productDocument.getProductId();
		this.name=productDocument.getName();
		this.price=productDocument.getPrice();
	}
}
