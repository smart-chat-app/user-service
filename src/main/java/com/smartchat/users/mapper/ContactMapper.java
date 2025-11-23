package com.smartchat.users.mapper;

import com.smartchat.users.message.model.NotificationInboundPayload;
import com.smartchat.users.persistance.user.model.Contact;

import java.util.UUID;

public class ContactMapper {

    public static Contact mapContact(NotificationInboundPayload msg){
        return Contact.builder()
                .notificationId(UUID.randomUUID().toString())
                .associatUsId(msg.getSenderUserId())
                .userId(msg.getReceiverUserId())
                .username(msg.getReceiverUsername())
                .bio(null)
                .build();
    }
}
