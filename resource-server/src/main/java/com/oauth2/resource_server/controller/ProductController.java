package com.oauth2.resource_server.controller;

import com.oauth2.resource_server.dto.SecurityUserContext;
import com.oauth2.resource_server.dto.UserContext;
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
        UserContext ctx = SecurityUserContext.get();
        assert ctx != null;
        System.out.println(ctx.getUsername());

        return ResponseEntity.ok("product/delete - bob only");
    }
}
