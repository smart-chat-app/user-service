package com.smartchat.users.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Slf4j
public class Utils {

    /**
     * Returns the authenticated userId coming from the gateway (X-User-Id header).
     * Throws 401 if missing.
     */
    public static String getUserId() {
        String userId = getHeaderUserId();
        if (userId == null || userId.isBlank()) {
            log.error("Missing X-User-Id header");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing X-User-Id header");
        }
        return userId;
    }

    private static String getHeaderUserId() {
        var attribute = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(attribute)) {
            //TODO throw an exception instead of null
            return null;
        }
        return attribute.getRequest().getHeader(ContextConstants.USER_ID); // e.g. "X-User-Id"
    }
}
