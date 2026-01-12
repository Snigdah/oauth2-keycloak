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

    @PreAuthorize("@kcAuth.hasPermission(#jwt.tokenValue, 'Product Resource', 'product:read')")
    @GetMapping
    public ResponseEntity<?> getProducts(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok("Products data");
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok("Product created");
    }

//
//    @PreAuthorize("@kcAuth.hasPermission(#jwt.tokenValue, 'Product Resource', 'product:create')")
//    @PostMapping
//    public ResponseEntity<?> createProduct(@AuthenticationPrincipal Jwt jwt) {
//        return ResponseEntity.ok("Product created");
//    }

//    @GetMapping
//    public ResponseEntity<?> getProducts() {
//        return ResponseEntity.ok("Products data");
//    }
//
//    @PostMapping
//    public ResponseEntity<?> createProduct() {
//        return ResponseEntity.ok("Product created");
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        return ResponseEntity.ok("Product deleted: " + id);
    }
}
