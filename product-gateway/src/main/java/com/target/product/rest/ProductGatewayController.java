package com.target.product.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.target.product.rest.data.Product;
import com.target.product.rest.service.ProductDataService;

@RestController
public class ProductGatewayController {
	protected Logger logger = LoggerFactory.getLogger(ProductGatewayController.class);

	@Autowired	
	private ProductDataService productDataService;
	
	@SuppressWarnings("unused")
	private ResponseEntity<Product> fallback(String id) throws Exception {
		logger.info("FALLBACK CALLED");
		Product product = productDataService.getProductWithoutName(id);
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }
    
	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
    @GetMapping("/products/{id}")
    @CrossOrigin
    @HystrixCommand(fallbackMethod = "fallback")
    public ResponseEntity<Product> getProduct(@PathVariable String id) throws Exception {
    	Product product = productDataService.getProductWithName(id);
    	return new ResponseEntity<Product>(product, HttpStatus.OK);    	                
    }

    /**
     * 
     * @param product
     * @param id
     * @return
     * @throws Exception
     */
    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product, @PathVariable String id) throws Exception {
    	product.setId(id);
    	Product result = productDataService.updateProduct(product);
    	return new ResponseEntity<Product>(result, HttpStatus.OK);    	                
    }
}

