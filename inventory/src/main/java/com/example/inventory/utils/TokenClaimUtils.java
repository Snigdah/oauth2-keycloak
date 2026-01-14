package com.example.inventory.utils;

import com.example.inventory.dto.TokenClaimResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.inventory.utils.Constants.TokenClaimUtils.*;


public class TokenClaimUtils {
    private static final Logger logger = LoggerFactory.getLogger(TokenClaimUtils.class);

    /**
     * Generic method to extract any claim (attribute) from the JWT token.
     *
     * @param claimName name of the claim to retrieve (e.g. "groupId", "branch", "orgId")
     * @param type      expected return type (e.g. Long.class, String.class)
     * @return Optional containing the value if found and castable, otherwise empty
     */
    public <T> Optional<T> getClaim(String claimName, Class<T> type) {
        Jwt jwt = getJwtToken();

        if (jwt == null) {
            return Optional.empty();
        }

        Map<String, Object> claims = jwt.getClaims();
        if (claims.containsKey(claimName)) {
            Object value = claims.get(claimName);

            try {
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

    public static TokenClaimResponse extractAllClaims() {
        Jwt jwt = getJwtToken();
        if (jwt == null) {
            return null;
        }

        Map<String, Object> claims = jwt.getClaims();

        var realmRoles = Optional.ofNullable(claims.get("realm_access"))
                .filter(Map.class::isInstance)
                .map(Map.class::cast)
                .map(m -> (List<String>) m.get("roles"))
                .orElse(List.of());

        return TokenClaimResponse.builder()
                .id(claims.getOrDefault("sub", "").toString())
                .username(claims.getOrDefault("preferred_username", "").toString())
                .email(claims.getOrDefault("email", "").toString())
                .firstName(claims.getOrDefault("given_name", "").toString())
                .lastName(claims.getOrDefault("family_name", "").toString())
                .fullName(claims.getOrDefault("name", "").toString())
                .employeeId(claims.getOrDefault( EMPLOYEE_ID, "").toString())
                .orgId(claims.getOrDefault(ORG_ID, "").toString())
                .officeId(claims.getOrDefault( OFFICE_ID, "").toString())
                .screenLockTime(claims.getOrDefault( SCREEN_LOCK_TIME, "").toString())
                .clickStreamTrack(claims.getOrDefault( CLICK_STREAM_TRACK, "").toString())
                .userTerminalIP((List<String>) claims.getOrDefault(USER_TERMINAL_IP, List.of()))
                .realmRoles(realmRoles)
                .build();
    }

    public static Jwt getJwtToken() {
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
}
