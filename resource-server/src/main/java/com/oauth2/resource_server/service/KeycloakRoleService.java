package com.oauth2.resource_server.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.oauth2.resource_server.utils.KeycloakUtils.TARGET_REALM;

@Service
public class KeycloakRoleService {

    private final Keycloak keycloak;
    private final String realmName;

    public KeycloakRoleService(Keycloak keycloak, String realmName) {
        this.keycloak = keycloak;
        this.realmName = realmName;
    }

    // Create Role
    public void createRole(String roleName, String description) {
        RoleRepresentation role = new RoleRepresentation();
        role.setName(roleName);
        role.setDescription(description);
        keycloak.realm(TARGET_REALM).roles().create(role);
    }

    // Get All Roles
    public List<RoleRepresentation> listRoles() {
        return keycloak.realm(TARGET_REALM).roles().list();
    }

    // Get Single Role
    public RoleRepresentation getRole(String roleName) {
        return keycloak.realm(TARGET_REALM).roles().get(roleName).toRepresentation();
    }

    // Update Role
    public void updateRole(String roleName, String newDescription) {
        var roleResource = keycloak.realm(TARGET_REALM).roles().get(roleName);
        RoleRepresentation existingRole = roleResource.toRepresentation();
        existingRole.setDescription(newDescription);
        roleResource.update(existingRole);
    }

    // Delete Role
    public void deleteRole(String roleName) {
        keycloak.realm(TARGET_REALM).roles().deleteRole(roleName);
    }
}
