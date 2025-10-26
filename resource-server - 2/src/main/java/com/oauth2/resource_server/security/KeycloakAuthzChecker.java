package com.oauth2.resource_server.security;

import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.springframework.stereotype.Service;

@Service
public class KeycloakAuthzChecker {

    private final AuthzClient authzClient;

    public KeycloakAuthzChecker(AuthzClient authzClient) {
        this.authzClient = authzClient;
    }

    /**
     * Ask Keycloak for permission for a resource + scope, passing the user's token as subject_token.
     */
    public boolean hasPermission(String accessToken, String resourceName, String scopeName) {
        try {
            AuthorizationRequest request = new AuthorizationRequest();
            request.addPermission(resourceName, scopeName);
            request.setSubjectToken(accessToken);

            AccessTokenResponse rpt = authzClient.authorization(accessToken).authorize(request);

            return rpt != null && rpt.getToken() != null;
        } catch (Exception e) {
            // For debugging, log e.getMessage(); but in prod consider specific handling
            return false;
        }
    }
}
