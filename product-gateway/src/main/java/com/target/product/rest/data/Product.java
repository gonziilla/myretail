package com.target.product.rest.data;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "products")
public class Product {
	public Product(String productId, String name, Map<String, String> currentPrice) {
		this.id = productId;
		this.name = name;
		this.currentPrice = currentPrice;
	}
	
	@Id
	private String id;
	
	@Transient //TODO Glenn : double check if need to be Transient
	private String name;
	
	private Map<String, String> currentPrice;
	
	
}
