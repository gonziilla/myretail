package com.target.product.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Component
public class UserFeignClientInterceptor implements RequestInterceptor {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN_TYPE = "Bearer";
    private final OAuth2AuthorizedClientService clientService;

    public UserFeignClientInterceptor(OAuth2AuthorizedClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public void apply(RequestTemplate template) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
        	JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        	Jwt accessToken = jwtAuthenticationToken.getToken();
	        template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, accessToken.getTokenValue()));
        } else {
	        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
	        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
	                oauthToken.getAuthorizedClientRegistrationId(),
	                oauthToken.getName());
	
	        OAuth2AccessToken accessToken = client.getAccessToken();
	        template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, accessToken.getTokenValue()));
        }
    }
}
