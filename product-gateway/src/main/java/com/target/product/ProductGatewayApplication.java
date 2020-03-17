package com.target.product;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.target.product.config.UserFeignClientInterceptor;
import com.target.product.repository.ProductRepository;
import com.target.product.rest.data.Product;
import com.target.product.zuul.AuthorizationHeaderFilter;

import feign.RequestInterceptor;

@EnableFeignClients
@EnableCircuitBreaker
@EnableDiscoveryClient
@EnableZuulProxy
@ComponentScan
//@EnableAutoConfiguration NOTE: auto configuration should already happen with SpringBootApplication
@SpringBootApplication
public class ProductGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductGatewayApplication.class, args);
	}

    @Configuration
    static class OktaOAuth2WebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                .authorizeRequests().anyRequest().authenticated()
                    .and()
                .oauth2Login()
                    .and()
                .oauth2ResourceServer().jwt();
            // @formatter:on
        }
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> simpleCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    @Bean
    public RequestInterceptor getUserFeignClientInterceptor(OAuth2AuthorizedClientService clientService) {
        return new UserFeignClientInterceptor(clientService);
    }

    @Bean
    public AuthorizationHeaderFilter authHeaderFilter(OAuth2AuthorizedClientService clientService) {
        return new AuthorizationHeaderFilter(clientService);
    }
    
    @Bean
    ApplicationRunner init(ProductRepository repository) {
        return args -> {
    		if (repository != null) {	
    			Map<String, String> cp1 = new HashMap<>();
    			cp1.put("currencyCode", "USD");
    			cp1.put("value", "13.49");
    			Product p1 = new Product("13860428", "", cp1);
    	
    			Map<String, String> cp2 = new HashMap<>();
    			cp2.put("currencyCode", "USD");
    			cp2.put("value", "29.99");
    			Product p2 = new Product("15117729", "", cp2);
    	
    			Map<String, String> cp3 = new HashMap<>();
    			cp3.put("currencyCode", "USD");
    			cp3.put("value", "19.99");
    			Product p3 = new Product("16483589", "", cp3);
    	
    			Map<String, String> cp4 = new HashMap<>();
    			cp4.put("currencyCode", "USD");
    			cp4.put("value", "89.99");
    			Product p4 = new Product("16696652", "", cp4);

    			Map<String, String> cp5 = new HashMap<>();
    			cp5.put("currencyCode", "USD");
    			cp5.put("value", "99.99");
    			Product p5 = new Product("16752456", "", cp4);

    			Map<String, String> cp6 = new HashMap<>();
    			cp6.put("currencyCode", "USD");
    			cp6.put("value", "189.99");
    			Product p6 = new Product("15643793", "", cp6);

    			Map<String, String> cp7 = new HashMap<>();
    			cp7.put("currencyCode", "PHP");
    			cp7.put("value", "499.89");
    			Product p7 = new Product("13860429", "", cp7);

    			Map<String, String> cp8 = new HashMap<>();
    			cp8.put("currencyCode", "PHP");
    			cp8.put("value", "499.89");
    			Product p8 = new Product("13860430", "", cp8);
    			
    			repository.deleteAll();		
    			
    			List<Product> products = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8);
    			repository.insert(products);
    		}
        };
    }    
}

