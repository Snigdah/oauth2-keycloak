package com.oauth2.resource_server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResourceInfo {
    private String name;
    private List<String> uris;
    private List<String> scopes;
    private Map<String, List<String>> attributes;
}
