package com.oauth2.resource_server.config;

import org.keycloak.adapters.authorization.PolicyEnforcer;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import jakarta.servlet.Filter;

@Configuration
@EnableConfigurationProperties(KeycloakProperties.class)
@ConditionalOnProperty(name = "keycloak.enabled", havingValue = "true")
public class KeycloakPolicyEnforcerConfig {

    @Bean
    @ConditionalOnProperty(name = "keycloak.enabled", havingValue = "true")
    public FilterRegistrationBean<Filter> keycloakPolicyEnforcerFilter(KeycloakProperties props) {
        org.keycloak.adapters.authorization.spi.ConfigurationResolver resolver = request -> buildPolicyEnforcerConfig(
                props);

        org.keycloak.adapters.authorization.integration.jakarta.ServletPolicyEnforcerFilter filter = new org.keycloak.adapters.authorization.integration.jakarta.ServletPolicyEnforcerFilter(
                resolver);
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>(filter);
        bean.addUrlPatterns("/*");
        bean.setOrder(1);
        return bean;
    }

    private AdapterConfig buildAdapterConfig(KeycloakProperties props) {
        AdapterConfig adapterConfig = new AdapterConfig();
        adapterConfig.setRealm(props.getRealm());
        adapterConfig.setAuthServerUrl(props.getAuthServerUrl());
        adapterConfig.setResource(props.getResource());

        // Set credentials
        if (props.getCredentials() != null && props.getCredentials().getSecret() != null) {
            adapterConfig.setCredentials(new java.util.HashMap<>());
            adapterConfig.getCredentials().put("secret", props.getCredentials().getSecret());
        }

        return adapterConfig;
    }

    private PolicyEnforcerConfig buildPolicyEnforcerConfig(KeycloakProperties props) {
        PolicyEnforcerConfig pec = new PolicyEnforcerConfig();

        // CRITICAL: Set the AdapterConfig so PolicyEnforcer knows how to connect to
        // Keycloak
        pec.setAuthServerUrl(props.getAuthServerUrl());
        pec.setRealm(props.getRealm());
        pec.setResource(props.getResource());
        if (props.getCredentials() != null && props.getCredentials().getSecret() != null) {
            pec.setCredentials(new java.util.HashMap<>());
            pec.getCredentials().put("secret", props.getCredentials().getSecret());
        }

        pec.setEnforcementMode(PolicyEnforcerConfig.EnforcementMode.valueOf(
                (props.getEnforcementMode() != null ? props.getEnforcementMode() : "ENFORCING").toUpperCase()));
        if (props.getLazyLoadPaths() != null) {
            pec.setLazyLoadPaths(props.getLazyLoadPaths());
        }

        // Enable http-method-as-scope: scopes are derived from HTTP methods (GET, POST,
        // PUT, DELETE)
        if (props.getHttpMethodAsScope() != null) {
            pec.setHttpMethodAsScope(props.getHttpMethodAsScope());
        }

        return pec;
    }
}
