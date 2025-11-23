package com.smartchat.users.persistance.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UserIdKey {
    private String userId;
    private String username;
}
