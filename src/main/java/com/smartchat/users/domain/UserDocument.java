package com.smartchat.users.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Setter
@Getter
@Document(collection = "users")
public class UserDocument {
    @Id
    private String id;
    private String userId;
    private String username;
    private String displayName;
    private String bio;
    private String avatarUrl;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

}
