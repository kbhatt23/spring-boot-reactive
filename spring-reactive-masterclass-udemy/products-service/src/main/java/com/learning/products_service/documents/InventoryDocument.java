package com.learning.products_service.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.learning.products_service.dtos.InventoryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = InventoryDocument.COLLECTION_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDocument {

	@Id
	private String id;
	
	@Field("product_id")
	private String productId;

	private int stock;
	
	public static final String COLLECTION_NAME = "inventory";
	
	public InventoryDocument(InventoryDTO inventoryDTO) {
		this.productId=inventoryDTO.getProductId();
		this.stock = inventoryDTO.getStock();
		this.id=inventoryDTO.getId();
	}
}
