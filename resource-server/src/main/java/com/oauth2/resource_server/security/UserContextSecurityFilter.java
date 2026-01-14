package com.oauth2.resource_server.security;

import com.oauth2.resource_server.dto.UserContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class UserContextSecurityFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        // Only trust headers if request is authenticated
        if (authentication instanceof JwtAuthenticationToken) {

            UserContext userContext = UserContext.builder()
                    .userId(request.getHeader("X-User-Id"))
                    .username(request.getHeader("X-Username"))
                    .email(request.getHeader("X-User-Email"))
                    .employeeId(request.getHeader("X-Employee-Id"))
                    .orgId(request.getHeader("X-Org-Id"))
                    .officeId(request.getHeader("X-Office-Id"))
                    .userTerminalIp(parse(request.getHeader("X-User-Terminal-IP")))
                    .build();

            request.setAttribute("USER_CONTEXT", userContext);
        }

        filterChain.doFilter(request, response);
    }

    private List<String> parse(String value) {
        if (value == null || value.isBlank()) return List.of();
        return Arrays.stream(value.split(",")).map(String::trim).toList();
    }
}
