package com.target.product.rest.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.target.product.config.UserFeignClientInterceptor;
import com.target.product.repository.ProductRepository;
import com.target.product.rest.ProductGatewayController;
import com.target.product.rest.data.Product;
import com.target.product.rest.feign.service.ProductNameFeignClient;
import com.target.product.rest.service.ProductDataService;

//TODO Glenn add integration tests
//TODO Glenn there should be a way to mock some of these effectively (RxClient?)
//@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProductGatewayController.class)
public class ProductGatewayControllerTest {
//    @TestConfiguration
//    static class TestContextConfiguration {
//    	@Bean
//        public ClientRegistrationRepository getClientRegistrationRepository() {
//            return Mockito.mock(ClientRegistrationRepository.class);
//        }
//    	@Bean
//    	public JwtDecoder getJwtDecoder() {
//            return Mockito.mock(JwtDecoder.class);
//    	}
//    	@Bean
//        public OAuth2AuthorizedClientService getOAuth2AuthorizedClientService() {
//            return Mockito.mock(OAuth2AuthorizedClientService.class);
//        }    	
//    	@Bean UserFeignClientInterceptor getInterceptor(OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
//    		return new UserFeignClientInterceptor(oAuth2AuthorizedClientService);
//    	}
//    }

//	@Autowired
//	private MockMvc mvc;
//	 
//	@MockBean
//	@Qualifier("productDataService")
//	private ProductDataService service;

	@Test
	public void testGetProduct() throws Exception {
//	    Product expected = new Product();
//	    expected.setId("999");
//	    expected.setName("Baby Elmo");
//	    Map<String, String> currentPrice = new HashMap<>();
//	    currentPrice.put("currencyCode", "XYX");
//	    currentPrice.put("value", "1234.45");
//	    expected.setCurrentPrice(currentPrice);
//	    
//		String expectedJson = "{\"productId\": \"999\",\"name\": \"Baby Elmo\",\"currentPrice\": {\"value\": \"1234.45\",\"currencyCode\": \"XYX\"}}";
//	    
//		Mockito.when(service.getProductWithName(Mockito.anyString())).thenReturn(expected);
//		
//		//String url = "/products/13860428";
//		//RequestBuilder requestBuilder = MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON_VALUE);
//		
////	    MvcResult result = mvc.perform(get("/products/999")
////	      .contentType(MediaType.APPLICATION_JSON)).andReturn();
////	    System.out.println("RESPONSE" + result.getResponse().getContentAsString());
////		JSONAssert.assertEquals(expectedJson, result.getResponse().getContentAsString(), false);
//
//		MvcResult result = mvc.perform(
//				get("/products/999").contentType(MediaType.APPLICATION_JSON)
//			).andExpect(status().isOk()).andReturn();
//		System.out.println("HERE'S THE RESULT" + result.getResponse().getContentAsString());
////	      .andExpect(jsonPath("$.id").value(expected.getId()))
////	      .andExpect(jsonPath("$.name").value(expected.getName()))
////	      .andExpect(jsonPath("$.currencyPrice.currencyCode").value(expected.getCurrentPrice().get("currencyCode")))
////	      .andExpect(jsonPath("$.currencyPrice.value").value(expected.getCurrentPrice().get("value")));
	}	
}
