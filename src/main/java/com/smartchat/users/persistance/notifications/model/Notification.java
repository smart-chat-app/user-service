package com.smartchat.users.persistance.notifications.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Builder
@AllArgsConstructor
@Document(collection = "notification")
public class Notification {
    private String notificationId;
    private String sender;
    private String userId;
}
