package com.oauth2.resource_server.security;

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

    public KeycloakAuthorizationFilter(KeycloakAuthzChecker keycloakAuthzChecker) {
        this.keycloakAuthzChecker = keycloakAuthzChecker;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // If endpoint is public or no mapping -> let it pass (or you can default to deny)
        String path = request.getRequestURI();
        String method = request.getMethod();

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof JwtAuthenticationToken jwtAuth)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid token");
            return;
        }

        String token = jwtAuth.getToken().getTokenValue();

        boolean allowed = false;
        if (keycloakAuthzChecker.hasPermission(token, path, method)) {
            allowed = true;
        }

        if (!allowed) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access denied by Keycloak");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean pathMatches(String requestPath, String mappingPath) {
        String regex = mappingPath.replaceAll("\\{[^/]+}", "[^/]+");
        return requestPath.matches(regex);
    }
}
