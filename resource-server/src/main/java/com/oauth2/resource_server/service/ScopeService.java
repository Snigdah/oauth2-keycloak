package com.oauth2.resource_server.service;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.authorization.ScopeRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

import static com.oauth2.resource_server.utils.KeycloakUtils.TARGET_REALM;

@Service
@RequiredArgsConstructor
public class ScopeService {
    private final Keycloak keycloak;
    private final String CLIENT_ID = "BFF";

    private String getClientUUID() {
        return keycloak.realm(TARGET_REALM)
                .clients()
                .findByClientId(CLIENT_ID)
                .get(0)
                .getId();
    }

    /**
     * Create a new scope
     */
    public String createScope(String name, String displayName, String iconUri) {
        String clientUUID = getClientUUID();

        ScopeRepresentation scope = new ScopeRepresentation();
        scope.setName(name);
        scope.setDisplayName(displayName);
        scope.setIconUri(iconUri);

        Response response = keycloak.realm(TARGET_REALM)
                .clients()
                .get(clientUUID)
                .authorization()
                .scopes()
                .create(scope);

        if (response.getStatus() == 201) {
            return "Scope created successfully";
        } else {
            return "Failed to create scope: " + response.getStatusInfo();
        }
    }

    /**
     * List all scopes
     */
    public List<ScopeRepresentation> listScopes() {
        String clientUUID = getClientUUID();
        return keycloak.realm(TARGET_REALM)
                .clients()
                .get(clientUUID)
                .authorization()
                .scopes()
                .scopes();
    }

    /**
     * Get scope by name
     */
    public ScopeRepresentation getScopeByName(String name) {
        String clientUUID = getClientUUID();

        return keycloak.realm(TARGET_REALM)
                .clients()
                .get(clientUUID)
                .authorization()
                .scopes()
                .findByName(name);
    }

    /**
     * Update an existing scope
     */
    public String updateScope(String name, String newDisplayName, String newIconUri) {
        String clientUUID = getClientUUID();
        ScopeRepresentation scope = keycloak.realm(TARGET_REALM)
                .clients()
                .get(clientUUID)
                .authorization()
                .scopes()
                .findByName(name);

        if (ObjectUtils.isEmpty(scope)) return "Scope not found";

        ScopeRepresentation existing = scope;
        if (newDisplayName != null) existing.setDisplayName(newDisplayName);
        if (newIconUri != null) existing.setIconUri(newIconUri);

        keycloak.realm(TARGET_REALM)
                .clients()
                .get(clientUUID)
                .authorization()
                .scopes()
                .scope(existing.getId())
                .update(existing);

        return "Scope updated successfully";
    }

    /**
     * Delete scope by name
     */
    public String deleteScope(String name) {
        String clientUUID = getClientUUID();
        ScopeRepresentation scope = keycloak.realm(TARGET_REALM)
                .clients()
                .get(clientUUID)
                .authorization()
                .scopes()
                .findByName(name);

        if (ObjectUtils.isEmpty(scope)) return "Scope not found";

        keycloak.realm(TARGET_REALM)
                .clients()
                .get(clientUUID)
                .authorization()
                .scopes()
                .scope(scope.getId())
                .remove();

        return "Scope deleted successfully";
    }
}
