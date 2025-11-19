package com.smartchat.users.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class Utils {

    public static String getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof Jwt jwt) {
            return jwt.getSubject();
        }

        return auth.getName();
    }
}
