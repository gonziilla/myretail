package com.target.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

//TODO Glenn : some error with 2.9.2 version not working with this version of SpringBoot. 
// had to use 3.0.0-SNAPSHOT but does not work still. 

@Configuration
//@EnableSwagger2
@EnableSwagger2WebMvc
public class SpringFoxConfig {                                    
	@Bean
	public Docket api() { 
	    return new Docket(DocumentationType.SWAGGER_2)  
	      .select()                                  
	      .apis(RequestHandlerSelectors.any())
	      .paths(PathSelectors.any())                          
	      .build();
	    
	    //.apis(RequestHandlerSelectors.basePackage("com.target.product.rest"))	      
	      
	}
}
