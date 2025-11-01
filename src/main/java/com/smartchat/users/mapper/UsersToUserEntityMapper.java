package com.smartchat.users.mapper;

import com.smartchat.users.message.model.UserMessage;
import com.smartchat.users.message.model.UserMessageOutboundPayload;
import com.smartchat.users.message.model.UserMessagePayload;
import com.smartchat.users.model.User;
import com.smartchat.users.persistance.model.UserEntity;

import java.net.URI;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class UsersToUserEntityMapper {
    public static UserEntity map(User user){
        return UserEntity.builder()
                .userId(UUID.randomUUID().toString())
                .bio(user.getBio().get())
                .avatarUrl(user.getAvatarUrl().get().toString())
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                //.createdAt(Instant.now())
                //.updatedAt(Instant.now())
                .build();

    }

    public static UserEntity mapFromKafka(UserMessageOutboundPayload message){
        return UserEntity.builder()
                .userId(UUID.randomUUID().toString())
                .bio(message.getBio().get())
                .avatarUrl(message.getAvatarUrl().get().toString())
                .username(message.getUsername())
                .displayName(message.getDisplayName().get())
                //.createdAt(message.getCreatedAt().toInstant())
                //.updatedAt(message.getUpdatedAt().toInstant())
                .build();
    }

    public static User mapFromEntity(UserEntity entity){
        return User.builder()
                .userId(entity.getUserId())
                .avatarUrl(URI.create(Objects.nonNull(entity.getAvatarUrl()) ? entity.getAvatarUrl() : ""))
                .bio(entity.getBio())
                //.createdAt(OffsetDateTime.from(entity.getCreatedAt()))
                .displayName(entity.getDisplayName())
                .username(entity.getUsername())
                //.updatedAt(OffsetDateTime.from(entity.getUpdatedAt()))
                .build();
    }
}
