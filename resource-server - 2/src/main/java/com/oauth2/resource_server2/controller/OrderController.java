package com.oauth2.resource_server2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@CrossOrigin("*")
public class OrderController {

    @GetMapping
    public ResponseEntity<?> getProducts() {
        return ResponseEntity.ok("Order data");
    }

    @PostMapping
    public ResponseEntity<?> createProduct() {
        return ResponseEntity.ok("Order created");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        return ResponseEntity.ok("Order deleted: " + id);
    }
}