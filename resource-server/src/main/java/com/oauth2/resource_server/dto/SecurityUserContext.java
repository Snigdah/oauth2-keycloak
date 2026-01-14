package com.oauth2.resource_server.dto;


import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * SecurityUserContext
 *
 * @author Mallika Dey
 */
public final class SecurityUserContext {

    private static final String KEY = "USER_CONTEXT";

    public static UserContext get() {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attrs == null) return null;

        return (UserContext) attrs.getRequest().getAttribute(KEY);
    }
}

