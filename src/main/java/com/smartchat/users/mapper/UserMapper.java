package com.smartchat.users.mapper;

import com.smartchat.users.message.model.UserMessageOutboundPayload;
import com.smartchat.users.model.Contacts;
import com.smartchat.users.model.User;
import com.smartchat.users.model.UserPublic;
import com.smartchat.users.persistance.user.model.Contact;
import com.smartchat.users.persistance.user.model.UserIdKey;
import com.smartchat.users.persistance.user.model.Users;

import java.net.URI;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public class UserMapper {
    public static Users mapResponse(User user){
        return Users.builder()
                .bio(checkBio(user))
                .avatarUrl(checkAvatarUrl(user))
                .userIdKey(buildUserIdKey(user.getUserId(), user.getUsername()))
                .displayName(user.getDisplayName())
                .updatedAt(Instant.now())
                .build();

    }

    public static Users mapFromKafka(UserMessageOutboundPayload message){
        return Users.builder()
                .userIdKey(buildUserIdKey(message.getUserId(), message.getUsername()))
                .bio(checkBioKafka(message))
                .avatarUrl(checkAvatarUrlKafka(message))
                .displayName(checkDisplayName(message))
                .createdAt(message.getCreatedAt().toInstant())
                .updatedAt(message.getUpdatedAt().toInstant())
                .build();
    }

    public static User mapDocument(Users entity){
        return User.builder()
                .userId(entity.getUserIdKey().getUserId())
                .avatarUrl(URI.create(Objects.nonNull(entity.getAvatarUrl()) ? entity.getAvatarUrl() : ""))
                .bio(entity.getBio())
                .displayName(entity.getDisplayName())
                .username(entity.getUserIdKey().getUsername())
                .contacts(null)
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

    private static UserIdKey buildUserIdKey(String userId, String username){
        return UserIdKey.builder()
                .userId(userId)
                .username(username)
                .build();
    }

    private static String checkBio(User user){
        return Optional.ofNullable(user.getBio().isPresent() ? user.getBio().get() : Optional.empty()).toString();
    }

    private static String checkAvatarUrl(User user){
        return Optional.ofNullable(user.getAvatarUrl().isPresent() ? user.getAvatarUrl().get() : Optional.empty()).toString();
    }

    private static String checkBioKafka(UserMessageOutboundPayload user){
        return Optional.ofNullable(user.getBio().isPresent() ? user.getBio().get() : Optional.empty()).toString();
    }

    private static String checkAvatarUrlKafka(UserMessageOutboundPayload user){
        return Optional.ofNullable(user.getAvatarUrl().isPresent() ? user.getAvatarUrl().get() : Optional.empty()).toString();
    }

    private static String checkDisplayName(UserMessageOutboundPayload user){
        return Optional.ofNullable(user.getDisplayName().isPresent() ? user.getDisplayName().get() : Optional.empty()).toString();
    }
}
