package com.target.product.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.target.product.rest.data.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
	
	public Product getProductById(String productId) throws Exception;
	
}
