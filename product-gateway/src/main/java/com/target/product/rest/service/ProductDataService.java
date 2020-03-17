package com.target.product.rest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.target.product.repository.ProductRepository;
import com.target.product.rest.data.Product;
import com.target.product.rest.exception.ProductNotFoundException;
import com.target.product.rest.feign.service.ProductNameFeignClient;

@Service
public class ProductDataService {
	protected Logger logger = LoggerFactory.getLogger(ProductDataService.class);

	@Autowired
	private ProductRepository productRepository;
	@Autowired
    private ProductNameFeignClient productNameFeignClient;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws ProductNotFoundException
	 */
    public Product getProductWithName(String id) throws ProductNotFoundException {
    	Product product = null;
    	try {
    		logger.info("Getting product and filling in name");
	    	product = productRepository.getProductById(id);
	    	product.setName(productNameFeignClient.getProductName(id).getBody());
    	} catch (Exception e) {
    		logger.error("Getting product failed", e);
    		throw new ProductNotFoundException(e);
    	}
    	return product;    	                
    }

    /**
     * 
     * @param id
     * @return
     * @throws ProductNotFoundException
     */
    public Product getProductWithoutName(String id) throws ProductNotFoundException {
    	Product product = null;
    	try {
    		logger.info("Getting product without name");
	    	product = productRepository.getProductById(id);
	    	product.setName("No name available");	    	
    	} catch (Exception e) {
    		logger.error("Getting product failed", e);
    		throw new ProductNotFoundException(e);
    	}
    	return product;    	                
    }
    
    /**
     * 
     * @param product
     * @return
     * @throws Exception
     */
    public Product updateProduct(Product product) throws Exception {
    	return productRepository.save(product);    	    	                
    }	
}


