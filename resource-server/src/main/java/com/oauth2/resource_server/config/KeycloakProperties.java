package com.oauth2.resource_server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {

    private boolean enabled = true;
    private String authServerUrl;
    private String realm;
    private String resource;
    private Credentials credentials = new Credentials();
    private Boolean lazyLoadPaths = true;
    private String enforcementMode = "ENFORCING";
    private List<PathEntry> paths = new ArrayList<>();

    public static class Credentials {
        private String secret;
        public String getSecret() { return secret; }
        public void setSecret(String secret) { this.secret = secret; }
    }

    public static class PathEntry {
        private String path;
        private List<MethodEntry> methods = new ArrayList<>();
        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
        public List<MethodEntry> getMethods() { return methods; }
        public void setMethods(List<MethodEntry> methods) { this.methods = methods; }
    }

    public static class MethodEntry {
        private String method;
        private List<String> scopes = new ArrayList<>();
        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
        public List<String> getScopes() { return scopes; }
        public void setScopes(List<String> scopes) { this.scopes = scopes; }
    }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public String getAuthServerUrl() { return authServerUrl; }
    public void setAuthServerUrl(String authServerUrl) { this.authServerUrl = authServerUrl; }
    public String getRealm() { return realm; }
    public void setRealm(String realm) { this.realm = realm; }
    public String getResource() { return resource; }
    public void setResource(String resource) { this.resource = resource; }
    public Credentials getCredentials() { return credentials; }
    public void setCredentials(Credentials credentials) { this.credentials = credentials; }
    public Boolean getLazyLoadPaths() { return lazyLoadPaths; }
    public void setLazyLoadPaths(Boolean lazyLoadPaths) { this.lazyLoadPaths = lazyLoadPaths; }
    public String getEnforcementMode() { return enforcementMode; }
    public void setEnforcementMode(String enforcementMode) { this.enforcementMode = enforcementMode; }
    public List<PathEntry> getPaths() { return paths; }
    public void setPaths(List<PathEntry> paths) { this.paths = paths; }
}
