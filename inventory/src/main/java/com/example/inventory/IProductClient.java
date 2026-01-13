package com.example.inventory;

import com.example.inventory.config.FeignOAuth2Config;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "productClient",
        url = "${product.service.url}",
        configuration = FeignOAuth2Config.class
)
public interface IProductClient {
    @PostMapping("/api/product")
    String createProduct();

    @GetMapping("/api/product")
    String getProducts();

}
