package br.com.mam.sgmc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    RestClient keycloakRestClient(OAuth2AuthorizedClientManager authorizedClientManager) {
        RestClient.Builder builder = RestClient.builder();
        var requestInterceptor = new OAuth2ClientHttpRequestInterceptor(authorizedClientManager);
        requestInterceptor.setClientRegistrationIdResolver((HttpRequest request) -> "sgmc");

        return builder
                .requestInterceptor(requestInterceptor)
                .build();
    }
}



