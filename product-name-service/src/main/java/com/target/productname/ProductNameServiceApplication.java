package com.target.productname;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//enable Hystrix
//register with Eureka
@EnableFeignClients 
@EnableCircuitBreaker 
@EnableDiscoveryClient 
@ComponentScan
@SpringBootApplication
public class ProductNameServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductNameServiceApplication.class, args);
	}

    @Configuration
    static class OktaOAuth2WebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                .authorizeRequests().anyRequest().authenticated()
                    .and()
                .oauth2ResourceServer().jwt();
            // @formatter:on
        }
    }
}
