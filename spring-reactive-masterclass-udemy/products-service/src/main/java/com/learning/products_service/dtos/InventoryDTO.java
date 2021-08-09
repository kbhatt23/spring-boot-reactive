package com.learning.products_service.dtos;

import com.learning.products_service.documents.InventoryDocument;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDTO {
	private String id;
	
	private String productId;

	private int stock;

	public InventoryDTO(InventoryDocument inventoryDocument) {
		this.productId=inventoryDocument.getProductId();
		this.stock = inventoryDocument.getStock();
		this.id=inventoryDocument.getId();
	}
}
