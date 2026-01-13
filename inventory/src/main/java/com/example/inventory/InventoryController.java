package com.example.inventory;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * InvetoryController
 *
 * @author Mallika Dey
 */
@RestController
@RequestMapping("/api/order")
public class InventoryController {
    private final IProductClient productClient;

    public InventoryController(IProductClient productClient) {
        this.productClient = productClient;
    }

    @PostMapping
    public ResponseEntity<?> createOrder() {
        productClient.createProduct();
        return ResponseEntity.ok("Inventory created");
    }
}
