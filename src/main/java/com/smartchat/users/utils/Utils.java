package com.smartchat.users.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Slf4j
public class Utils {

    public static String getUserId() {
        String userId = getHeaderUserId();
        if (Objects.isNull(userId) || userId.isEmpty()) {
            log.error("UserId is mandatory");
            return null;
        }
        return userId;
    }

    public static void checkUserId(String pathUserId) {
        String headerUserId = getHeaderUserId();
        if (!headerUserId.equals(pathUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Attempt to inject another user has been made");
        }
    }

    private static String getHeaderUserId() {
        var attribute = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(attribute)) {
            return null;
        }

        return attribute.getRequest().getHeader(ContextConstants.USER_ID);
    }
}
