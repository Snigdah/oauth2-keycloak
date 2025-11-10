package com.oauth2.resource_server.service;

import com.oauth2.resource_server.dto.EditPolicyDTO;
import com.oauth2.resource_server.dto.PolicyDTO;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.authorization.PolicyRepresentation;
import org.keycloak.representations.idm.authorization.RolePolicyRepresentation;
import org.keycloak.representations.idm.authorization.UserPolicyRepresentation;
import org.keycloak.representations.idm.authorization.GroupPolicyRepresentation;
import org.keycloak.representations.idm.authorization.JSPolicyRepresentation;
import org.keycloak.representations.idm.authorization.TimePolicyRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.oauth2.resource_server.utils.KeycloakUtils.TARGET_REALM;

@Service
@RequiredArgsConstructor
public class PolicyService {
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
     * Create a Policy (Role-based, User-based, Group-based, JS, Time, etc.)
     */
    public String createPolicy(PolicyDTO dto) {
        String clientUUID = getClientUUID();

        PolicyRepresentation policy = buildPolicyRepresentation(dto);

        Response response = keycloak.realm(TARGET_REALM)
                .clients()
                .get(clientUUID)
                .authorization()
                .policies()
                .create(policy);

        if (response.getStatus() == 201) {
            return "Policy created successfully";
        } else {
            return "Failed to create policy: " + response.getStatusInfo();
        }
    }

    /**
     * List all policies for the client
     */
    public List<PolicyRepresentation> listPolicies() {
        String clientUUID = getClientUUID();
        return keycloak.realm(TARGET_REALM)
                .clients()
                .get(clientUUID)
                .authorization()
                .policies()
                .policies();
    }

    /**
     * Get a policy by name
     */
    public PolicyRepresentation getPolicyByName(String policyName) {
        String clientUUID = getClientUUID();

        return keycloak.realm(TARGET_REALM)
                .clients()
                .get(clientUUID)
                .authorization()
                .policies()
                .findByName(policyName);
    }

    /**
     * Update an existing policy
     */
    public String updatePolicy(String policyName, EditPolicyDTO dto) {
        String clientUUID = getClientUUID();
        PolicyRepresentation oldPolicy = keycloak.realm(TARGET_REALM)
                .clients()
                .get(clientUUID)
                .authorization()
                .policies()
                .findByName(policyName);

        if (ObjectUtils.isEmpty(oldPolicy)) return "Policy not found";

        PolicyRepresentation newPolicy = buildPolicyRepresentation(PolicyDTO
                .builder()
                .name(policyName)
                .description(dto.getDescription())
                .type(dto.getType())
                .logic(dto.getLogic())
                .roles(dto.getRoles())
                .users(dto.getUsers())
                .groups(dto.getGroups())
                .notBefore(dto.getNotBefore())
                .notOnOrAfter(dto.getNotOnOrAfter())
                .build());
        newPolicy.setId(oldPolicy.getId());

        keycloak.realm(TARGET_REALM)
                .clients()
                .get(clientUUID)
                .authorization()
                .policies()
                .policy(newPolicy.getId())
                .update(newPolicy);

        return "Policy updated successfully";
    }

    /**
     * Delete a policy by name
     */
    public String deletePolicy(String policyName) {
        String clientUUID = getClientUUID();
        PolicyRepresentation policy = keycloak.realm(TARGET_REALM)
                .clients()
                .get(clientUUID)
                .authorization()
                .policies()
                .findByName(policyName);

        if (ObjectUtils.isEmpty(policy)) return "Policy not found";

        keycloak.realm(TARGET_REALM)
                .clients()
                .get(clientUUID)
                .authorization()
                .policies()
                .policy(policy.getId())
                .remove();

        return "Policy deleted successfully";
    }

    /**
     * Internal: Build PolicyRepresentation based on DTO type
     */
    private PolicyRepresentation buildPolicyRepresentation(PolicyDTO dto) {
        PolicyRepresentation policy = new PolicyRepresentation();
        policy.setName(dto.getName());
        policy.setDescription(dto.getDescription());
        policy.setType(dto.getType());
        policy.setLogic(dto.getLogic());

        switch (dto.getType().toLowerCase()) {
            case "role":
                Set<RolePolicyRepresentation.RoleDefinition> roleDefinitions = getRoleDefinition(dto.getRoles());
                String rolesJson = roleDefinitions.stream()
                        .map(r -> String.format("{\"id\":\"%s\",\"required\":true}", r.getId()))
                        .collect(Collectors.joining(",", "[", "]"));

                policy.setConfig(Map.of("roles", rolesJson));
                break;

            case "user":
                policy.setConfig(Map.of(
                        "users", String.join(",", dto.getUsers())
                ));
                break;

            case "group":
                policy.setConfig(Map.of(
                        "groups", String.join(",", dto.getGroups())
                ));
                break;

           /* case "js":
                policy.setConfig(Map.of(
                        "js", String.join(",", dto.getUsers())
                ));
                break;*/

            case "time":
                policy.setConfig(Map.of(
                        "notBefore", dto.getNotBefore(),
                        "notOnOrAfter", dto.getNotOnOrAfter()
                ));
                break;

            default:
                throw new IllegalArgumentException("Unsupported policy type: " + dto.getType());
        }
        return policy;
    }

    private Set<RolePolicyRepresentation.RoleDefinition> getRoleDefinition(Set<String> roles) {
        Set<RolePolicyRepresentation.RoleDefinition> roleDefinitions = new HashSet<>();
        for (String roleName : roles) {
            // Fetch the role from Keycloak
            var role = keycloak.realm(TARGET_REALM).roles().get(roleName).toRepresentation();
            if (role != null) {
                RolePolicyRepresentation.RoleDefinition rd = new RolePolicyRepresentation.RoleDefinition();
                rd.setId(role.getId());
                rd.setRequired(Boolean.TRUE);
                roleDefinitions.add(rd);
            }
        }
        return roleDefinitions;
    }
}
