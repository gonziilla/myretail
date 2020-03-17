package com.target.productname.rest.service.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.target.productname.rest.feign.service.RedskyClient;
import com.target.productname.rest.service.ProductNameService;

@RunWith(SpringRunner.class)
public class ProductNameServiceTest {
    @TestConfiguration
    static class ProductDataServiceTestContextConfiguration {
        @Bean
        public ProductNameService getProductDataService() {
            return new ProductNameService();
        }
    }
    
	@Autowired
	private ProductNameService productNameService;
	 
	@MockBean
	private RedskyClient redskyClient;
	
	@Test
	public void testGetProductName() throws Exception {
		String expectedId = "1"; 
		String expectedJson = "{\"product\": {\"item\": {\"product_description\": {\"title\": \"Spongebob Squarepants\"}}}}";
		String expectedTitle = "Spongebob Squarepants";
		ResponseEntity<String> expectedResponseEntity = new ResponseEntity<>(expectedJson, HttpStatus.OK);
		
		Mockito.when(redskyClient.getProductById(Mockito.anyString())).thenReturn(expectedResponseEntity);
		
		String result = productNameService.getProductName(expectedId);
		
        assertEquals("Response returned the correct title", expectedTitle, result);	    
	}
}
