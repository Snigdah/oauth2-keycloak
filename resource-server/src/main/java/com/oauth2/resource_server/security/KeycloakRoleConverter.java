package com.oauth2.resource_server.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;

/**
 * KeycloakRoleConverter
 *
 * @author Mallika Dey
 */
public class KeycloakRoleConverter
        implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        Map<String, Object> resourceAccess =
                jwt.getClaim("resource_access");

        if (resourceAccess == null) {
            return authorities;
        }

        Map<String, Object> product =
                (Map<String, Object>) resourceAccess.get("Product");

        if (product == null) {
            return authorities;
        }

        List<String> roles =
                (List<String>) product.get("roles");

        if (roles == null) {
            return authorities;
        }

        roles.forEach(role ->
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role))
        );

        return authorities;
    }
}