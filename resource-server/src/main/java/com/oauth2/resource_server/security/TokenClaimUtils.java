package com.oauth2.resource_server.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import javax.swing.text.html.Option;
import java.util.Map;
import java.util.Optional;

public class TokenClaimUtils {
    private static final Logger logger = LoggerFactory.getLogger(TokenClaimUtils.class);

    /**
     * Get the user's email from the jwt access token.
     *
     * @return user's email, or null if not found
     */
    public static String getEmail() {
        Jwt jwt = getJwtToken();
        if (jwt == null) {
            return null;
        }
        Map<String, Object> claims = jwt.getClaims();
        Object email = claims.get("email");
        if (email != null) {
            return email.toString();
        }
        return null;
    }

    /**
     * Get the user's name from the jwt access token.
     *
     * @return user's name, or "anonymous" if not found
     */
    public static String getUserName() {
        Jwt jwt = getJwtToken();
        if (jwt == null) {
            return null;
        }
        Map<String, Object> claims = jwt.getClaims();
        Object name = claims.get("name");
        if (name != null) {
            return name.toString();
        }
        return "anonymous";
    }

    /**
     * Generic method to extract any claim (attribute) from the JWT token.
     *
     * @param claimName name of the claim to retrieve (e.g. "groupId", "branch", "orgId")
     * @param type      expected return type (e.g. Long.class, String.class)
     * @return Optional containing the value if found and castable, otherwise empty
     */
    public static <T> Optional<T> getClaim(String claimName, Class<T> type) {
        Jwt jwt = getJwtToken();

        if (jwt == null) {
            return Optional.empty();
        }

        Map<String, Object> claims = jwt.getClaims();
        if (claims.containsKey(claimName)) {
            Object value = claims.get(claimName);

            try {
                // Handle numeric strings (e.g., "123" → Long)
                if (type.equals(Long.class) && value instanceof String) {
                    return Optional.of(type.cast(Long.valueOf((String) value)));
                }
                return Optional.of(type.cast(value));
            } catch (ClassCastException | NumberFormatException e) {
                logger.error("Failed to cast claim '{}' to type {}: {}", claimName, type.getSimpleName(), e.getMessage());
            }
        } else {
            logger.warn("Claim '{}' not found in token", claimName);
        }

        return Optional.empty();
    }

    private static Jwt getJwtToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            logger.error("No authentication found");
            return null;
        }

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken();
        }

        logger.error("Authentication is not an instance of JwtAuthenticationToken");
        return null;
    }
}
