package com.oauth2.resource_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDTO {
    private String name;
    private String displayName;
    private List<String> scopes;
    private Map<String, List<String>> attributes;
    private boolean ownerManagedAccess = true;
}
