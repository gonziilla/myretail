package com.target.productname.rest.feign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "redsky", url = "https://redsky.target.com/v1/pdp/tcin/")
public interface RedskyClient {
    @GetMapping("{id}?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics")
    ResponseEntity<String> getProductById(@PathVariable String id);
}
