package com.smartchat.users.events.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartchat.users.message.model.NotificationHeader;
import com.smartchat.users.message.model.NotificationMessage;
import com.smartchat.users.message.model.NotificationPayload;
import com.smartchat.users.model.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
public class SendNotificationProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final static String NOTIFICATION_TOPIC = "notification.created";
    private final static String DLQ_TOPIC = "notification.created.dlq";

    private final ObjectMapper mapper;

    @Autowired
    public SendNotificationProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper mapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.mapper = mapper;
    }

    public void pushNotification(Notification notification) {
        Objects.requireNonNull(notification, "user must not be null");

        String json = mapMessage(notification);
        log.info("json {}", json);
        try {
            log.info("Sending message for userId {}", notification.getUserId());
            kafkaTemplate.send(NOTIFICATION_TOPIC, notification.getUserId(), json);
        } catch (Exception e) {
            log.error("Problem to send the message - sent to DLQ{}", e.getMessage());
            kafkaTemplate.send(DLQ_TOPIC, notification.getUserId(), json);
        }
    }

    private NotificationMessage buildNotification(Notification notification) {
        NotificationHeader header = NotificationHeader.builder()
                .eventId(UUID.randomUUID())
                .build();
        NotificationPayload payload = NotificationPayload.builder() //TODO Add senderUserId in the payload
                .senderUsername(notification.getSenderUsername())
                .receiverUsername(notification.getSenderUsername())
                .build();
        return NotificationMessage.builder()
                .header(header)
                .payload(payload)
                .build();
    }

    private String mapMessage(Notification message) {
        NotificationMessage msg = buildNotification(message);
        log.info("msg {}", msg);
        try {
            return mapper.writeValueAsString(msg);
        } catch (JsonProcessingException e) {
            log.error("Impossible to parse {}", e.getMessage());
            return null;
        }
    }

}
