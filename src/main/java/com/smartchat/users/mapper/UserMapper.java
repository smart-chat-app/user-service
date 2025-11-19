package com.smartchat.users.mapper;

import com.smartchat.users.message.model.UserMessageOutboundPayload;
import com.smartchat.users.model.Contacts;
import com.smartchat.users.model.User;
import com.smartchat.users.model.UserPublic;
import com.smartchat.users.persistance.user.model.Contact;
import com.smartchat.users.persistance.user.model.Users;

import java.net.URI;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class UserMapper {
    public static Users mapResponse(User user){
        return Users.builder()
                .bio(user.getBio().get())
                .avatarUrl(user.getAvatarUrl().get().toString())
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .updatedAt(Instant.now())
                .build();

    }

    public static Users mapFromKafka(UserMessageOutboundPayload message){
        return Users.builder()
                .userId(UUID.randomUUID().toString())
                .bio(message.getBio().get())
                .avatarUrl(message.getAvatarUrl().get().toString())
                .username(message.getUsername())
                .displayName(message.getDisplayName().get())
                .createdAt(message.getCreatedAt().toInstant())
                .updatedAt(message.getUpdatedAt().toInstant())
                .build();
    }

    public static User mapDocument(Users entity){
        return User.builder()
                .userId(entity.getUserId())
                .avatarUrl(URI.create(Objects.nonNull(entity.getAvatarUrl()) ? entity.getAvatarUrl() : ""))
                .bio(entity.getBio())
                .createdAt(OffsetDateTime.from(entity.getCreatedAt()))
                .displayName(entity.getDisplayName())
                .username(entity.getUsername())
                .contacts(null)
                .updatedAt(OffsetDateTime.from(entity.getUpdatedAt()))
                .build();
    }

    public static Contacts mapContact(Contact contact){
        return Contacts.builder()
                .userId(contact.getUserId())
                .bio(contact.getBio())
                .displayName(contact.getUsername())
                .username(contact.getUsername())
                .build();
    }

    public static UserPublic maUserPublic(User user){
        return UserPublic.builder()
                .bio(user.getBio())
                .displayName(user.getDisplayName())
                .username(user.getUsername())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }
}
