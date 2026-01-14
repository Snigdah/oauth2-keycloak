package com.example.inventory.config;


import com.example.inventory.dto.TokenClaimResponse;
import com.example.inventory.utils.TokenClaimUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
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

            TokenClaimResponse claims = TokenClaimUtils.extractAllClaims();
            if (claims == null) {
                return;
            }

            addHeader(requestTemplate, "X-User-Id", claims.getId());
            addHeader(requestTemplate, "X-Username", claims.getUsername());
            addHeader(requestTemplate, "X-User-Email", claims.getEmail());
            addHeader(requestTemplate, "X-User-FullName", claims.getFullName());
            addHeader(requestTemplate, "X-Employee-Id", claims.getEmployeeId());
            addHeader(requestTemplate, "X-Org-Id", claims.getOrgId());
            addHeader(requestTemplate, "X-Office-Id", claims.getOfficeId());
            addHeader(requestTemplate, "X-Screen-Lock-Time", claims.getScreenLockTime());
            addHeader(requestTemplate, "X-ClickStream-Track", claims.getClickStreamTrack());

            if (claims.getUserTerminalIP() != null && !claims.getUserTerminalIP().isEmpty()) {
                requestTemplate.header(
                        "X-User-Terminal-IP",
                        String.join(",", claims.getUserTerminalIP())
                );
            }
        };
    }

    private void addHeader(RequestTemplate template, String name, String value) {
        if (value != null && !value.isBlank()) {
            template.header(name, value);
        }
    }
}

