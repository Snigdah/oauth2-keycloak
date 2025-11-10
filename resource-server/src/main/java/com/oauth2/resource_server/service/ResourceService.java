package com.oauth2.resource_server.service;

import com.oauth2.resource_server.dto.ResourceDTO;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.keycloak.representations.idm.authorization.ScopeRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.oauth2.resource_server.utils.KeycloakUtils.TARGET_REALM;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final Keycloak keycloak;// Your realm
    private final String CLIENT_ID = "BFF";    // Your client

    private String getClientUUID() {
        return keycloak.realm(TARGET_REALM)
                .clients()
                .findByClientId(CLIENT_ID)
                .get(0)
                .getId();
    }

    private ClientResource getClient(String clientUUID) {
        return keycloak.realm(TARGET_REALM)
                .clients()
                .get(clientUUID);

    }

    // Create Resource
    public String createResource(ResourceDTO dto) {
        String clientUUID = getClientUUID();

        ResourceRepresentation resource = new ResourceRepresentation();
        resource.setName(dto.getName());
        resource.setDisplayName(dto.getDisplayName());
        resource.setOwnerManagedAccess(dto.isOwnerManagedAccess());
        if (dto.getAttributes() != null && !dto.getAttributes().isEmpty()) {
            resource.setAttributes(dto.getAttributes());
        }

        Response response = keycloak.realm(TARGET_REALM)
                .clients()
                .get(clientUUID)
                .authorization()
                .resources()
                .create(resource);

        return response.getStatus() == 201 ? "Resource created successfully" : "Failed: " + response.getStatusInfo();
    }

    // List Resources
    public List<ResourceRepresentation> listResources() {
        String clientUUID = getClientUUID();
        return keycloak.realm(TARGET_REALM)
                .clients()
                .get(clientUUID)
                .authorization()
                .resources()
                .resources();
    }

    // Get Resource by Name
    public ResourceRepresentation getResourceByName(String resourceName) {
        String clientUUID = getClientUUID();
        List<ResourceRepresentation> resources = keycloak.realm(TARGET_REALM)
                .clients()
                .get(clientUUID)
                .authorization()
                .resources()
                .findByName(resourceName);

        return resources.isEmpty() ? null : resources.get(0);
    }

    // Update Resource
    public String updateResource(String resourceName, ResourceDTO dto) {
        String clientUUID = getClientUUID();
        ClientResource client = getClient(clientUUID);

        List<ResourceRepresentation> resources = keycloak.realm(TARGET_REALM)
                .clients()
                .get(clientUUID)
                .authorization()
                .resources()
                .findByName(resourceName);

        if (resources.isEmpty()) return "Resource not found";

        ResourceRepresentation existing = resources.get(0);
        if (dto.getDisplayName() != null) existing.setDisplayName(dto.getDisplayName());
        if (dto.getAttributes() != null) existing.setAttributes(dto.getAttributes());
        existing.setOwnerManagedAccess(dto.isOwnerManagedAccess());

        if (dto.getScopes() != null && !dto.getScopes().isEmpty()) {
            Set<ScopeRepresentation> updatedScopes = new HashSet<>();

            for (String scopeName : dto.getScopes()) {
                var auth = client.authorization();
                List<ScopeRepresentation> foundScopes = Collections
                        .singletonList(
                                auth
                                        .scopes()
                                        .findByName(scopeName));
                if (!ObjectUtils.isEmpty(foundScopes)) {
                    updatedScopes.add(foundScopes.getFirst());
                }
                /*
                //won't support this for now
                else {
                    // Optional: create the scope if it doesn't exist
                    ScopeRepresentation newScope = new ScopeRepresentation();
                    newScope.setName(scopeName);
                    auth.scopes().create(newScope);
                    updatedScopes.add(newScope);
                }*/
            }

            existing.setScopes(updatedScopes);
        }

        keycloak.realm(TARGET_REALM)
                .clients()
                .get(clientUUID)
                .authorization()
                .resources()
                .resource(existing.getId())
                .update(existing);

        return "Resource updated successfully";
    }

    // Delete Resource
    public String deleteResource(String resourceName) {
        String clientUUID = getClientUUID();
        List<ResourceRepresentation> resources = keycloak.realm(TARGET_REALM)
                .clients()
                .get(clientUUID)
                .authorization()
                .resources()
                .findByName(resourceName);

        if (resources.isEmpty()) return "Resource not found";

        keycloak.realm(TARGET_REALM)
                .clients()
                .get(clientUUID)
                .authorization()
                .resources()
                .resource(resources.get(0).getId())
                .remove();

        return "Resource deleted successfully";
    }
}
