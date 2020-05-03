package com.target.product.rest.service.test;

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

import com.target.product.repository.ProductRepository;
import com.target.product.rest.data.Product;
import com.target.product.rest.exception.ProductNotFoundException;
import com.target.product.rest.feign.service.ProductNameFeignClient;
import com.target.product.rest.service.ProductDataService;


@RunWith(SpringRunner.class)
public class ProductDataServiceTest {
    @TestConfiguration
    static class ProductDataServiceTestContextConfiguration {
  
        @Bean
        public ProductDataService getProductDataService() {
            return new ProductDataService();
        }
    }

	@Autowired
	private ProductDataService productDataService;
	
	@MockBean
	private ProductRepository productRepository;
	
	@MockBean
	private ProductNameFeignClient productNameFeignClient;
	
	
	@Test
    public void testGetProductWithName() throws Exception {
		String expectedId = "13424324";
	    Product expected = new Product();
	    expected.setId(expectedId);
	    
	    String expectedName = "xyz brand";
	    ResponseEntity<String> expectedResponse = new ResponseEntity<String>(expectedName, HttpStatus.OK);
	    
		Mockito.when(productRepository.getProductById(Mockito.anyString())).
			thenReturn(expected);
		Mockito.when(productNameFeignClient.getProductName(Mockito.anyString())).
			thenReturn(expectedResponse);
		
	    Product result = productDataService.getProductWithName(expectedId);
	 
	    Mockito.verify(productRepository).getProductById(expectedId);
	    Mockito.verify(productNameFeignClient).getProductName(expectedId);
        assertEquals("Repo returned the correct id", expected.getId(), result.getId());
        assertEquals("Response returned the correct name", result.getName(), expectedName);	    
	}

	@Test(expected = ProductNotFoundException.class)
    public void testGetProductWithName_throwsFeignClientError() throws Exception {
		String expectedId = "non existent product";
	    Product expected = new Product();

		Mockito.when(productRepository.getProductById(Mockito.anyString())).
			thenReturn(expected);
		Mockito.when(productNameFeignClient.getProductName(Mockito.anyString())).
			thenThrow(new Exception("feign error"));
		
	    productDataService.getProductWithName(expectedId);
	    
	    Mockito.verify(productRepository).getProductById(expectedId);
	}
	
	@Test(expected = ProductNotFoundException.class)
    public void testGetProductWithName_throwsRepoError() throws Exception {
		String expected = "non existent product";
		Mockito.when(productRepository.getProductById(Mockito.anyString())).
			thenThrow(new Exception("repo error"));
		
	    productDataService.getProductWithName(expected);
	    
	    Mockito.verify(productRepository).getProductById(expected);
	    Mockito.verifyNoInteractions(productNameFeignClient);
	    
	}
	
	@Test
    public void testGetProductWithoutName() throws Exception {
	    String expectedId = "13424324";
	    Product expected = new Product();
	    expected.setId(expectedId);	    
	    String expectedName = "No name available";
	    
		Mockito.when(productRepository.getProductById(Mockito.anyString())).
			thenReturn(expected);
		
	    Product result = productDataService.getProductWithoutName(expectedId);
	 
	    Mockito.verify(productRepository).getProductById(expectedId);
        assertEquals("Repo returned the correct id", expected.getId(), result.getId());
        assertEquals("Response returned the correct name", result.getName(), expectedName);	    
	}
	
	@Test(expected = ProductNotFoundException.class)
    public void testGetProductWithoutName_throwsError() throws Exception {
		String expected = "non existent product";
		Mockito.when(productRepository.getProductById(Mockito.anyString())).
			thenThrow(new Exception("some error"));
		
	    productDataService.getProductWithoutName(expected);
	    
	    Mockito.verify(productRepository).getProductById(expected);
	}

	@Test
    public void testUpdateProduct() throws Exception {
		String expectedId = "13424324";
	    Product expected = new Product();
	    expected.setId(expectedId);
	    
		Mockito.when(productRepository.save(Mockito.any())).thenReturn(expected);
		
		Product result = productDataService.updateProduct(expected);
	    
		Mockito.verify(productRepository).getProductById(expectedId);
	    Mockito.verify(productRepository).save(expected);
        assertEquals("Repo returned the correct id", expected.getId(), result.getId());
	}
	
	@Test(expected = ProductNotFoundException.class)
    public void testUpdateProduct_ProductDoesNotExist() throws Exception {
		Product expected = new Product();
	    expected.setId("999");

		Mockito.when(productRepository.getProductById(Mockito.anyString())).
			thenThrow(new ProductNotFoundException());

		productDataService.updateProduct(expected);
		
		Mockito.verify(productRepository, Mockito.never()).save(Mockito.any());
	}	
	
}
