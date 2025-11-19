package com.smartchat.users.mapper;

import com.smartchat.users.message.model.NotificationInboundPayload;
import com.smartchat.users.persistance.notifications.model.Notification;

import java.util.UUID;

public class NotificationMapper {
    public static Notification map(NotificationInboundPayload payload){
        return Notification.builder()
                .notificationId(UUID.randomUUID().toString())
                .sender(payload.getSenderUsername())
                .build();
    }
}
