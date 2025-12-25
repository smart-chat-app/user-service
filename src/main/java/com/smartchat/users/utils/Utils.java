package com.smartchat.users.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
                .orElse("");
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
