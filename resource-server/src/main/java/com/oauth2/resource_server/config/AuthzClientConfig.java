package com.oauth2.resource_server.config;

import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.springframework.context.annotation.Bean;

import java.util.Map;

@org.springframework.context.annotation.Configuration
public class AuthzClientConfig {

    @Bean
    public AuthzClient authzClient() {
        return AuthzClient.create(new Configuration(
                "http://localhost:9080", // auth-server-url
                "OneBank",              // realm
                "ERP-App",              // resource/client-id
                Map.of("secret", "VywnDnjHVTLLm6C7jK7lEPokwraT6ERY"), // credentials
                null
        ));
    }
}
