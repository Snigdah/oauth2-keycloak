package com.oauth2.resource_server.controller;

import com.oauth2.resource_server.service.ScopeService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.authorization.ScopeRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scopes")
@RequiredArgsConstructor
public class ScopeController {
    private final ScopeService scopeService;

    @PostMapping
    public ResponseEntity<String> createScope(
            @RequestParam String name,
            @RequestParam(required = false) String displayName,
            @RequestParam(required = false) String iconUri) {
        String message = scopeService.createScope(name, displayName, iconUri);
        return ResponseEntity.ok(message);
    }

    @GetMapping
    public ResponseEntity<List<ScopeRepresentation>> listScopes() {

        List<ScopeRepresentation> scopes = scopeService.listScopes();
        return ResponseEntity.ok(scopes);
    }

    @GetMapping("/{name}")
    public ResponseEntity<ScopeRepresentation> getScopeByName(@PathVariable("name") String name) {
        ScopeRepresentation scope = scopeService.getScopeByName(name);
        return scope != null ? ResponseEntity.ok(scope) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{name}")
    public ResponseEntity<String> updateScope(
            @PathVariable("name") String name,
            @RequestParam(required = false) String displayName,
            @RequestParam(required = false) String iconUri) {
        String message = scopeService.updateScope(name, displayName, iconUri);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteScope(@PathVariable("name") String name) {
        String message = scopeService.deleteScope(name);
        return ResponseEntity.ok(message);
    }
}
