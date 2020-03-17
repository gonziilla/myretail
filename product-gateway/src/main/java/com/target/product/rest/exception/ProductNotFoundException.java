package com.target.product.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Product not found. ")
public class ProductNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public ProductNotFoundException(Throwable cause) {
		super(cause);
	}
}
