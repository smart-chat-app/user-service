package com.smartchat.users.persistance.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Builder
@AllArgsConstructor
@Document(collection = "contacts")
public class Contact {
    @Id
    private String userId;
    private String notificationId;
    private String username;
    private String bio;
    private String associatUsId;

}
