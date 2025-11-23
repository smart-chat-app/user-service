package com.smartchat.users.persistance.user.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "contacts")
public class Contact {
    @Id
    private String userId;
    private String notificationId;
    private String username;
    private String bio;
    private String associatUsId;

}
