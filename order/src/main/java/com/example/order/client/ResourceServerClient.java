package com.example.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "resource-server", url = "${resource.server.url}", configuration = FeignConfig.class)
public interface ResourceServerClient {

    @GetMapping("/product/read/history")
    String readHistory();

    @PostMapping("/product/create/today")
    String createToday();

    @DeleteMapping("/product/delete")
    String deleteProduct();
}
