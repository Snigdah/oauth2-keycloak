package com.oauth2.bff.config;

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
                "bff",              // resource/client-id
                Map.of("secret", "VTF5RtMNWaN3mqdkLiFdCXszRNDTOwQ9"), // credentials
                null
        ));
    }
}
