package com.smartchat.users.persistance.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@Document(collection = "users")
public class Users {
    @Id
    @Indexed(unique = true)
    private String id;
    private String userId;
    private String username;
    private String displayName;
    private String bio;
    private String avatarUrl;
/*    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();*/

}
