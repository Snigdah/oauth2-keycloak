package com.oauth2.resource_server.controller;

import com.oauth2.resource_server.service.KeycloakRoleService;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final KeycloakRoleService roleService;

    public RoleController(KeycloakRoleService roleService) {
        this.roleService = roleService;
    }

    // Create
    @PostMapping
    public ResponseEntity<String> createRole(@RequestParam String name,
                                             @RequestParam(required = false) String description) {
        roleService.createRole(name, description);
        return ResponseEntity.ok("Role created successfully: " + name);
    }

    // Read All
    @GetMapping
    public ResponseEntity<List<RoleRepresentation>> listRoles() {
        return ResponseEntity.ok(roleService.listRoles());
    }

    // Read One
    @GetMapping("/{name}")
    public ResponseEntity<RoleRepresentation> getRole(@PathVariable String name) {
        return ResponseEntity.ok(roleService.getRole(name));
    }

    // Update
    @PutMapping("/{name}")
    public ResponseEntity<String> updateRole(@PathVariable String name,
                                             @RequestParam String description) {
        roleService.updateRole(name, description);
        return ResponseEntity.ok("Role updated successfully: " + name);
    }

    // Delete
    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteRole(@PathVariable String name) {
        roleService.deleteRole(name);
        return ResponseEntity.ok("Role deleted successfully: " + name);
    }
}
