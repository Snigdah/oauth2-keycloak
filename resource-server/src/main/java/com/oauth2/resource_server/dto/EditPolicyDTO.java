package com.oauth2.resource_server.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.keycloak.representations.idm.authorization.Logic;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EditPolicyDTO {
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
