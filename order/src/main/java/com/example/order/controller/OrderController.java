package com.example.order.controller;

import com.example.order.client.ResourceServerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final ResourceServerClient resourceServerClient;

    @GetMapping("/history")
    public ResponseEntity<?> readHistory() {
        return ResponseEntity.ok(resourceServerClient.readHistory());
    }

    @GetMapping("")
    public ResponseEntity<?> createHistory() {
        return ResponseEntity.ok(resourceServerClient.createToday());
    }

    @DeleteMapping("")
    public ResponseEntity<?> delete() {
        return ResponseEntity.ok(resourceServerClient.deleteProduct());
    }
}
