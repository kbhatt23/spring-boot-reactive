package com.learning.products_service.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.learning.products_service.dtos.ProductDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = ProductDocument.COLLECTION_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDocument {

	@Id
	private String productId;

	@Field("product_name")
	private String name;
	
	@Field("product_price")
	private double price;
	
	public static final String COLLECTION_NAME = "products";
	
	public ProductDocument(ProductDTO productDTO) {
		this.productId=productDTO.getProductId();
		this.name = productDTO.getName();
		this.price=productDTO.getPrice();
	}
}
