package com.smartchat.users.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class NotificationDTO {
    private String notificationId;
    private String sender;
}
