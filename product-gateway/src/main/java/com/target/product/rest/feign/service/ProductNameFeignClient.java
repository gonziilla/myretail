package com.target.product.rest.feign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("product-name-service")
public interface ProductNameFeignClient {
    @GetMapping("/product-names/{id}")
    @CrossOrigin
    ResponseEntity<String> getProductName(@PathVariable String id) throws Exception;
}