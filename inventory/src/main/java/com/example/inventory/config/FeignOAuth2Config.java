package com.example.inventory.config;


import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

/**
 * FeignOAuth2Config
 *
 * @author Mallika Dey
 */
@Configuration
public class FeignOAuth2Config {

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository registrations,
            OAuth2AuthorizedClientService clientService) {

        OAuth2AuthorizedClientProvider provider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager manager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        registrations, clientService);

        manager.setAuthorizedClientProvider(provider);
        return manager;
    }

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor(
            OAuth2AuthorizedClientManager manager) {

        return requestTemplate -> {
            OAuth2AuthorizeRequest authorizeRequest =
                    OAuth2AuthorizeRequest.withClientRegistrationId("keycloak")
                            .principal("inventory-service")
                            .build();

            OAuth2AuthorizedClient client =
                    manager.authorize(authorizeRequest);

            String token = client.getAccessToken().getTokenValue();

            requestTemplate.header(
                    HttpHeaders.AUTHORIZATION,
                    "Bearer " + token
            );
        };
    }
}

