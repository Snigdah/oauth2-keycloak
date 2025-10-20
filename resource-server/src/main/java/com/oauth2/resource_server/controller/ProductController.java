package com.oauth2.resource_server.controller;

import com.oauth2.resource_server.security.KeycloakAuthzChecker;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductController {

    @GetMapping
    public ResponseEntity<?> getProducts() {
        return ResponseEntity.ok("Products Get API Working");
    }

    @PostMapping
    public ResponseEntity<?> createProduct() {
        return ResponseEntity.ok("Product Created API Working");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        return ResponseEntity.ok("Product deleted: " + id);
    }
}
