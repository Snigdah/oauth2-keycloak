package com.oauth2.resource_server.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.keycloak.representations.idm.authorization.Logic;
import org.keycloak.representations.idm.authorization.RolePolicyRepresentation;

import java.util.List;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PolicyDTO {
    @NotEmpty
    private String name;
    private String description;
    @NotEmpty
    private String type;
    private Logic logic = Logic.POSITIVE;

    // For role-based
    private Set<String> roles;

    // For user-based
    private List<String> users;

    // For group-based
    private List<String> groups;

    // For JS-based
   // private String script;

    // For time-based
    private String notBefore;     // ISO datetime
    private String notOnOrAfter;
}
