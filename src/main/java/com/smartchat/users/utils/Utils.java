package com.smartchat.users.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Slf4j
public class Utils {

    /**
     * Returns the authenticated userId coming from the gateway (X-User-Id header).
     * Throws 401 if missing.
     */
    public static String getUserId() {
        return getHeaderUserId()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing X-User-Id header"));
    }

    private static Optional<String> getHeaderUserId() {
        return Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(attr -> attr.getRequest().getHeader(ContextConstants.USER_ID))
                .flatMap(Utils::validateUserId);
    }


    private static Optional<String> validateUserId(String userId) {
        try {
            UUID.fromString(userId);
            return Optional.of(userId);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
