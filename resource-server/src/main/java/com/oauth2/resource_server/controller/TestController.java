package com.oauth2.resource_server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v3")
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<?> getTest(){
        return ResponseEntity.ok("end point working");
    }
}
