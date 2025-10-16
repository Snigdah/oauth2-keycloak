package com.oauth2.resource_server.security;

import com.oauth2.resource_server.config.AuthorizationMappingConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class KeycloakAuthorizationFilter extends OncePerRequestFilter {

    private final KeycloakAuthzChecker keycloakAuthzChecker;
    private final AuthorizationMappingConfig mappingConfig;

    public KeycloakAuthorizationFilter(KeycloakAuthzChecker keycloakAuthzChecker,
                                       AuthorizationMappingConfig mappingConfig) {
        this.keycloakAuthzChecker = keycloakAuthzChecker;
        this.mappingConfig = mappingConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // If endpoint is public or no mapping -> let it pass (or you can default to deny)
        String path = request.getRequestURI();
        String method = request.getMethod();

        AuthorizationMappingConfig.Mapping mapping = mappingConfig.getMappings().stream()
                .filter(m -> pathMatches(path, m.getPath()) && method.equalsIgnoreCase(m.getMethod()))
                .findFirst()
                .orElse(null);

        if (mapping != null) {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (!(auth instanceof JwtAuthenticationToken jwtAuth)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid token");
                return;
            }

            String token = jwtAuth.getToken().getTokenValue();

            boolean allowed = false;
            for (String scope : mapping.getScopes().split(",")) {
                if (keycloakAuthzChecker.hasPermission(token, mapping.getResource(), scope.trim())) {
                    allowed = true;
                    break;
                }
            }

            if (!allowed) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied by Keycloak");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean pathMatches(String requestPath, String mappingPath) {
        String regex = mappingPath.replaceAll("\\{[^/]+}", "[^/]+");
        return requestPath.matches(regex);
    }
}
