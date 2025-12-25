package com.smartchat.users.mapper;

import com.smartchat.users.message.model.NotificationInboundPayload;
import com.smartchat.users.model.Notification;
import com.smartchat.users.persistance.notifications.model.NotificationEntity;

import java.util.UUID;

public class NotificationMapper {
    public static NotificationEntity map(NotificationInboundPayload payload){
        return NotificationEntity.builder()
                .notificationId(UUID.randomUUID().toString())
                .sender(payload.getSenderUsername())
                .userId(payload.getReceiverUserId())
                .build();
    }

    public static Notification mapDTO(NotificationEntity notificationEntity){
        return Notification.builder()
                .senderUsername(notificationEntity.getSender())
                .build();
    }
}
