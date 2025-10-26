package com.smartchat.users.mapper;

import com.smartchat.users.model.User;
import com.smartchat.users.persistance.model.UserEntity;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class UsersToUserEntityMapper {
    public static UserEntity map(User user){
        return UserEntity.builder()
                .userId(UUID.randomUUID().toString())
                .bio(Objects.isNull(user.getBio().get()) ? "" : user.getBio().get())
                .avatarUrl(Objects.isNull(user.getAvatarUrl().get().toString()) ? "" : user.getAvatarUrl().get().toString())
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

    }
}
