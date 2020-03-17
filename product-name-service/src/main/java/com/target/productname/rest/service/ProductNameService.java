package com.target.productname.rest.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.productname.rest.data.Product;
import com.target.productname.rest.feign.service.RedskyClient;

@Service
public class ProductNameService {
	@Autowired
    private RedskyClient redskyClient;

    public String getProductName(String id) throws Exception {
    	String json = redskyClient.getProductById(id).getBody();
    	System.out.println("JSON : " + json);
    	return parseProductTitle(json);    	                
    }

	protected String parseProductTitle(String json) throws JsonMappingException, JsonProcessingException, IOException {
    	Product product = new ObjectMapper().readValue(json, Product.class);
    	
    	@SuppressWarnings("unchecked")
		Map<String, Object> item = (Map<String, Object>) product.getProduct().get("item");
    	
    	@SuppressWarnings("unchecked")
		Map<String, Object> productDescription = (Map<String, Object>) item.get("product_description");
    	return (String) productDescription.get("title");    	
    }
}
