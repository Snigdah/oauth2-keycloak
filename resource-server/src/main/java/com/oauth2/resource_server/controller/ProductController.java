package com.oauth2.resource_server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    @GetMapping("/create/today")
    public ResponseEntity<String> createToday() {
        return ResponseEntity.ok("product/create/today - Manager only");
    }

    @GetMapping("/read/history")
    public ResponseEntity<String> readHistory() {
        return ResponseEntity.ok("product/read/history - Supervisor only");
    }
}
