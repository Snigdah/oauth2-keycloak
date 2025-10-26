package com.oauth2.resource_server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
