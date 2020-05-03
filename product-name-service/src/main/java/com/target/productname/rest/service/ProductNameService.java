package com.target.productname.rest.service;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.productname.rest.data.Product;
import com.target.productname.rest.feign.service.RedskyClient;

@Service
public class ProductNameService {
	protected Logger logger = LoggerFactory.getLogger(ProductNameService.class);

	@Autowired
    private RedskyClient redskyClient;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
    public String getProductName(String id) throws Exception {
    	String json = redskyClient.getProductById(id).getBody();
    	logger.info("JSON : " + json);
    	return parseProductTitle(json);    	                
    }

    /**
     * 
     * @param json
     * @return
     * @throws JsonParseException
     * @throws IOException
     */
	public String parseProductTitle(String json) throws JsonParseException, IOException {
    	Product product = new ObjectMapper().readValue(json, Product.class);
    	
    	@SuppressWarnings("unchecked")
		Map<String, Object> item = (Map<String, Object>) product.getProduct().get("item");
    	
    	@SuppressWarnings("unchecked")
		Map<String, Object> productDescription = (Map<String, Object>) item.get("product_description");
    	return (String) productDescription.get("title");    	
    }
}
