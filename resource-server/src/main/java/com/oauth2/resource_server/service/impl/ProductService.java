package com.oauth2.resource_server.service.impl;

import com.oauth2.resource_server.security.TokenClaimUtils;
import com.oauth2.resource_server.service.IProductService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProductService implements IProductService {
    @Override
    public String getProductOwnerName() {
        //usage example
        String name = TokenClaimUtils.getUserName();
        String email = TokenClaimUtils.getEmail();
        Map<String, Object> resourceAccess = TokenClaimUtils.getClaim("resource_access", Map.class).orElse(Map.of());
        return name;
    }
}
