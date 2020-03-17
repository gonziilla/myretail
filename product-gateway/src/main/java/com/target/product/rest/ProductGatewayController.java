package com.target.product.rest;

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
	
	@Autowired	
	private ProductDataService productDataService;
	
	
	@SuppressWarnings("unused")
	private ResponseEntity<Product> fallback(String id) throws Exception {
		System.out.println("FALLBACK CALLED");
	    	
		Product product = productDataService.getProductWithoutName(id);

        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }
    
    @GetMapping("/products/{id}")
    @CrossOrigin
    @HystrixCommand(fallbackMethod = "fallback")
    public ResponseEntity<Product> getProduct(@PathVariable String id) throws Exception {
    	Product product = productDataService.getProductWithName(id);
    	return new ResponseEntity<Product>(product, HttpStatus.OK);    	                
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product, @PathVariable String id) throws Exception {
    	product.setId(id);
    	Product result = productDataService.updateProduct(product);
    	return new ResponseEntity<Product>(result, HttpStatus.OK);    	                
    }
}

