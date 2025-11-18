package com.smartchat.users.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Objects;

public class Utils {

    public static String getUserIdFromToken(String token) {
        if (Objects.isNull(token)) {
            throw new RuntimeException("Empty token, not possible to retrieve userId");
        }
        return token.length() > 26 ? token.substring(0, 26) : token;
    }

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
