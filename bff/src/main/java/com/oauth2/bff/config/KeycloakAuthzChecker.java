package com.oauth2.bff.config;

import com.oauth2.bff.dto.ResourceInfo;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.representation.TokenIntrospectionResponse;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.keycloak.representations.idm.authorization.Permission;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class KeycloakAuthzChecker {

    private final AuthzClient authzClient;

    public KeycloakAuthzChecker(AuthzClient authzClient) {
        this.authzClient = authzClient;
    }

    /**
     * Ask Keycloak for permission for a resource + scope, passing the user's token as subject_token.
     * Filters resources based on x-client-id header.
     */
    public List<ResourceInfo> getAccessibleResources(String accessToken, HttpServletRequest request) {
        try {
            AuthorizationRequest authRequest = new AuthorizationRequest();
            authRequest.setSubjectToken(accessToken);

            AuthorizationResponse response = authzClient.authorization(accessToken).authorize(authRequest);
            String rpt = response.getToken();

            // Introspect the RPT to get permissions granted
            TokenIntrospectionResponse introspection =
                    authzClient.protection().introspectRequestingPartyToken(rpt);

            List<Permission> permissions = introspection.getPermissions();
            if (ObjectUtils.isEmpty(permissions)) {
                return new ArrayList<>();
            }

            // Build full resource list
            List<ResourceInfo> allResources = new ArrayList<>();
            for (Permission perm : permissions) {
                String resourceId = perm.getResourceId();
                if (resourceId != null) {
                    ResourceRepresentation resource = authzClient.protection().resource().findById(resourceId);
                    if (resource != null) {
                        allResources.add(
                                new ResourceInfo(
                                        resource.getName(),
                                        resource.getUris() != null ? new ArrayList<>(resource.getUris()) : new ArrayList<>(),
                                        perm.getScopes() != null ? new ArrayList<>(perm.getScopes()) : new ArrayList<>(),
                                        resource.getAttributes()
                                )
                        );
                    }
                }
            }

            // Filter by x-client-id attribute
            String clientIdHeader = request.getHeader("x-client-id");
            if (clientIdHeader == null) return allResources; // no filtering if header missing

            List<ResourceInfo> filtered = new ArrayList<>();
            for (ResourceInfo r : allResources) {
                // Assuming you stored x-client-id in resource attributes
                List<String> attrClientIds = r.getAttributes() != null ? r.getAttributes().get("x-client-id") : null;
                if (attrClientIds != null && attrClientIds.contains(clientIdHeader)) {
                    filtered.add(r);
                }
            }

            return filtered;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
