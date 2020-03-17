package com.target.productname.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.target.productname.rest.service.ProductNameService;

@RestController
public class ProductNameController {
	@Autowired
	private ProductNameService productNameService;
	
	@SuppressWarnings("unused")
	private ResponseEntity<String> fallback(String id) throws Exception {
		System.out.println("FALLBACK CALLED");
		return new ResponseEntity<String>("No data available from redsky", HttpStatus.OK);
    }
    
    @GetMapping("/product-names/{id}")
    @HystrixCommand(fallbackMethod = "fallback")
    public ResponseEntity<String> getProductName(@PathVariable String id) throws Exception {
    	String title = productNameService.getProductName(id);
    	System.out.println("PRODUCT TITLE : " + title);
    	
    	return new ResponseEntity<String>(title, HttpStatus.OK);    	                
    }
}
