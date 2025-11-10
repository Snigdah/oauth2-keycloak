package com.oauth2.resource_server.controller;

import com.oauth2.resource_server.dto.ResourceDTO;
import com.oauth2.resource_server.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping
    public ResponseEntity<String> createResource(@RequestBody ResourceDTO dto) {
        return ResponseEntity.ok(resourceService.createResource(dto));
    }

    @GetMapping
    public ResponseEntity<List<ResourceRepresentation>> listResources() {
        return ResponseEntity.ok(resourceService.listResources());
    }

    @GetMapping("/{name}")
    public ResponseEntity<ResourceRepresentation> getResource(@PathVariable String name) {
        ResourceRepresentation resource = resourceService.getResourceByName(name);
        return resource != null ? ResponseEntity.ok(resource) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{name}")
    public ResponseEntity<String> updateResource(@PathVariable String name, @RequestBody ResourceDTO dto) {
        return ResponseEntity.ok(resourceService.updateResource(name, dto));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteResource(@PathVariable String name) {
        return ResponseEntity.ok(resourceService.deleteResource(name));
    }
}