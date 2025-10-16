package com.oauth2.resource_server.controller;

import com.oauth2.resource_server.security.KeycloakAuthzChecker;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @GetMapping
    public ResponseEntity<?> getProducts() {
        return ResponseEntity.ok("Products data");
    }

    @PostMapping
    public ResponseEntity<?> createProduct() {
        return ResponseEntity.ok("Product created");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        return ResponseEntity.ok("Product deleted: " + id);
    }
}
