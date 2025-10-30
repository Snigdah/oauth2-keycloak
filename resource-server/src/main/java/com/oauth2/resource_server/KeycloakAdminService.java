package com.oauth2.resource_server;

import com.oauth2.resource_server.dto.UserRegistrationDto;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.core.Response;
import org.apache.catalina.User;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserProfileAttributeMetadata;
import org.keycloak.representations.idm.UserProfileMetadata;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeycloakAdminService {
    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    private Keycloak keycloak;

    @PostConstruct
    public void init() {
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType("client_credentials")
                .build();
    }

    public String createUser(UserRegistrationDto dto) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        UserRepresentation user = new UserRepresentation();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEnabled(true);

        // Custom attributes
        Map<String, List<String>> attributes = new HashMap<>();
//        attributes.put("phoneNumber", List.of(dto.getPhoneNumber()));
//        attributes.put("address", List.of(dto.getAddress()));
//        attributes.put("branchId", List.of(dto.getBranchId()));
//        metadata.setAttributes(attributes);
//        user.setUserProfileMetadata();
        user.setAttributes(attributes);

        Response response = usersResource.create(user);
        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create user: " + response.getStatusInfo());
        }

        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        // Set password
        CredentialRepresentation password = new CredentialRepresentation();
        password.setType(CredentialRepresentation.PASSWORD);
        password.setValue(dto.getPassword());
        password.setTemporary(false);

        usersResource.get(userId).resetPassword(password);

        return userId;
    }
}
