package com.oauth2.resource_server.security.interceptor;

import com.oauth2.resource_server.security.KeycloakAuthzChecker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final KeycloakAuthzChecker authzChecker;

    public AuthorizationInterceptor(KeycloakAuthzChecker authzChecker) {
        this.authzChecker = authzChecker;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod hm)) {
            return true;
        }

        // Controller method name
        String methodName = hm.getMethod().getName();

        // HTTP method -> scope
        String scope = request.getMethod();

        // Extract JWT from SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String token = jwt.getTokenValue();

        boolean permitted =
                authzChecker.hasPermission(token, methodName, scope);

        if (!permitted) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Permission denied");
            return false;
        }

        return true;
    }
}
