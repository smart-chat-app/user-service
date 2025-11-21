package com.smartchat.users.mapper;

import com.smartchat.users.dto.notification.NotificationDTO;
import com.smartchat.users.message.model.NotificationInboundPayload;
import com.smartchat.users.persistance.notifications.model.Notification;

import java.util.UUID;

public class NotificationMapper {
    public static Notification map(NotificationInboundPayload payload){
        return Notification.builder()
                .notificationId(UUID.randomUUID().toString())
                .sender(payload.getSenderUsername())
                .userId(payload.getReceiverUserId())
                .build();
    }

    public static NotificationDTO mapDTO(Notification notification){
        return NotificationDTO.builder()
                .sender(notification.getSender())
                .build();
    }
}
