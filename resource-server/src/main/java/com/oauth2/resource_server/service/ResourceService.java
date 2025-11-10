package com.oauth2.resource_server.service;

import com.oauth2.resource_server.dto.ResourceDTO;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final Keycloak keycloak;
    private final String TARGET_REALM = "OneBank"; // Your realm
    private final String CLIENT_ID = "Product";    // Your client

    private String getClientUUID() {
        return keycloak.realm(TARGET_REALM)
                .clients()
                .findByClientId(CLIENT_ID)
                .get(0)
                .getId();
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
