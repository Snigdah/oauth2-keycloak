package com.oauth2.resource_server.config;

import org.keycloak.adapters.authorization.PolicyEnforcer;
import org.keycloak.adapters.authorization.integration.jakarta.ServletPolicyEnforcerFilter;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import jakarta.servlet.Filter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties(KeycloakProperties.class)
@ConditionalOnProperty(name = "keycloak.enabled", havingValue = "true")
public class KeycloakPolicyEnforcerConfig {

    @Bean
    public FilterRegistrationBean<Filter> keycloakPolicyEnforcerFilter(KeycloakProperties props) {
        org.keycloak.adapters.authorization.spi.ConfigurationResolver resolver = request -> buildPolicyEnforcerConfig(
                props);

        ServletPolicyEnforcerFilter filter =
                new ServletPolicyEnforcerFilter(resolver);

        FilterRegistrationBean<Filter> bean =
                new FilterRegistrationBean<>(filter);

        // 🔥 PEP is active, but paths control WHERE it applies
        bean.addUrlPatterns("/*");
        bean.setOrder(1);
        return bean;
    }

    private PolicyEnforcerConfig buildPolicyEnforcerConfig(KeycloakProperties props) {

        PolicyEnforcerConfig pec = new PolicyEnforcerConfig();

        pec.setAuthServerUrl(props.getAuthServerUrl());
        pec.setRealm(props.getRealm());
        pec.setResource(props.getResource());

        if (props.getCredentials() != null &&
                props.getCredentials().getSecret() != null) {

            pec.setCredentials(new java.util.HashMap<>());
            pec.getCredentials().put(
                    "secret",
                    props.getCredentials().getSecret()
            );
        }

        // 🔥 GLOBAL DEFAULT → PEP ENABLED
        pec.setEnforcementMode(
                PolicyEnforcerConfig.EnforcementMode.valueOf(
                        props.getEnforcementMode().toUpperCase() // ENFORCING
                )
        );

        pec.setLazyLoadPaths(props.getLazyLoadPaths());
        pec.setHttpMethodAsScope(props.getHttpMethodAsScope());

        // 🔓 ONLY EXCLUSIONS ARE CONFIGURED HERE
        if (props.getPaths() != null && !props.getPaths().isEmpty()) {

            List<PolicyEnforcerConfig.PathConfig> pathConfigs =
                    new ArrayList<>();

            for (KeycloakProperties.PathEntry p : props.getPaths()) {

                PolicyEnforcerConfig.PathConfig pathConfig =
                        new PolicyEnforcerConfig.PathConfig();

                pathConfig.setPath(p.getPath());

                // 🔥 EXPLICITLY DISABLE PEP FOR THESE PATHS
                pathConfig.setEnforcementMode(
                        PolicyEnforcerConfig.EnforcementMode.DISABLED
                );

                pathConfigs.add(pathConfig);
            }

            pec.setPaths(pathConfigs);
        }

        return pec;
    }
}