package com.oauth2.resource_server.config;

import com.oauth2.resource_server.dto.ResourceInfo;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.representation.TokenIntrospectionResponse;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.keycloak.representations.idm.authorization.Permission;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class KeycloakAuthzChecker {

    private final AuthzClient authzClient;

    public KeycloakAuthzChecker(AuthzClient authzClient) {
        this.authzClient = authzClient;
    }

    /**
     * Ask Keycloak for permission for a resource + scope, passing the user's token as subject_token.
     */
    public List<ResourceInfo> getAccessibleResources(String accessToken) {
        try {
            AuthorizationRequest request = new AuthorizationRequest();
            request.setSubjectToken(accessToken);

            AuthorizationResponse response = authzClient.authorization(accessToken).authorize(request);

            String rpt = response.getToken();

            // Introspect the RPT to get permissions granted
            TokenIntrospectionResponse introspection =
                    authzClient.protection().introspectRequestingPartyToken(rpt);

            List<Permission> permissions = introspection.getPermissions();
            if (ObjectUtils.isEmpty(permissions)) {
                return new ArrayList<>();
            }

            // Resolve resources and build result list
            List<ResourceInfo> accessibleResources = new ArrayList<>();

            for (Permission perm : permissions) {
                String resourceId = perm.getResourceId();
                if (resourceId != null) {
                    ResourceRepresentation resource = authzClient.protection().resource().findById(resourceId);

                    if (resource != null) {
                        accessibleResources.add(
                                new ResourceInfo(
                                        resource.getName(),
                                        resource.getUris() != null ? new ArrayList<>(resource.getUris()) : new ArrayList<>(),
                                        perm.getScopes() != null ? new ArrayList<>(perm.getScopes()) : new ArrayList<>()
                                )
                        );
                    }
                }
            }
            return accessibleResources;

            //  Deduplicate by resource name (optional)
//            return accessibleResources.stream()
//                    .collect(Collectors.collectingAndThen(
//                            Collectors.toMap(ResourceInfo::getName, r -> r, (r1, r2) -> r1),
//                            m -> new ArrayList<>(m.values())
//                    ));
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
