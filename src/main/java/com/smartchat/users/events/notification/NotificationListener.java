package com.smartchat.users.events.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartchat.users.mapper.ContactMapper;
import com.smartchat.users.message.model.NotificationInboundMessage;
import com.smartchat.users.persistance.ContactPersistance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationListener {

    private final ObjectMapper mapper;
    private final ContactPersistance contactPersistance;
    private static final String NOTIFICATION_TOPIC = "notification.processed";

    @Autowired
    public NotificationListener(ObjectMapper mapper, ContactPersistance contactPersistance) {
        this.mapper = mapper;
        this.contactPersistance = contactPersistance;
    }

    @KafkaListener(topics = NOTIFICATION_TOPIC, groupId = "notificatioId")
    public void addContacts(String message) {
        NotificationInboundMessage msg = mapMessage(message);
        log.info("Message incoming {}", message);
        if(msg.getPayload().getAccepted()) {
            contactPersistance.saveContact(ContactMapper.mapContact(msg.getPayload()));
        }
    }

    private NotificationInboundMessage mapMessage(String message) {
        try {
            return mapper.readValue(message, NotificationInboundMessage.class);
        } catch (JsonProcessingException e) {
            log.error("Impossible to parse message {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
