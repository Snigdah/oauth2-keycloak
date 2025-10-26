package com.oauth2.resource_server.controller;

import com.oauth2.resource_server.security.KeycloakAuthzChecker;
import com.oauth2.resource_server.security.TokenClaimUtils;
import com.oauth2.resource_server.service.IProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<?> getProducts() {
        String ownerName = productService.getProductOwnerName();
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
