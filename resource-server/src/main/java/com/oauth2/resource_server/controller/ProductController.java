package com.oauth2.resource_server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @PostMapping("/create/today")
    public ResponseEntity<String> createToday() {
        return ResponseEntity.ok("product/create/today - Manager only");
    }

    @GetMapping("/read/history")
    public ResponseEntity<String> readHistory() {
        return ResponseEntity.ok("product/read/history - Supervisor only");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteProduct() {
        return ResponseEntity.ok("product/delete - bob only");
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('manager')")
    public ResponseEntity<String> patchTest() {
        return ResponseEntity.ok("client access");
    }
}
