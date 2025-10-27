package com.oauth2.resource_server.controller;

import com.oauth2.resource_server.config.KeycloakAuthzChecker;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("test")
public class TestController {

    public final KeycloakAuthzChecker keycloakAuthzChecker;

    public TestController(KeycloakAuthzChecker keycloakAuthzChecker) {
        this.keycloakAuthzChecker = keycloakAuthzChecker;
    }

    @GetMapping("/accessible-resource")
    public ResponseEntity<?> getAccessibleResources(@AuthenticationPrincipal Jwt jwt, HttpServletRequest request) {
        String accessToken = jwt.getTokenValue();
        return ResponseEntity.ok(keycloakAuthzChecker.getAccessibleResources(accessToken, request));
    }
}
