package com.smartchat.users.events.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartchat.users.mapper.ContactMapper;
import com.smartchat.users.message.model.NotificationInboundMessage;
import com.smartchat.users.message.model.NotificationInboundPayload;
import com.smartchat.users.persistance.notifications.NotificationPersistance;
import com.smartchat.users.persistance.notifications.model.Notification;
import com.smartchat.users.persistance.user.ContactPersistence;
import com.smartchat.users.service.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.smartchat.users.mapper.NotificationMapper.map;
import static com.smartchat.users.utils.ContextConstants.NOTIFICATION_TOPIC;
import static com.smartchat.users.utils.ContextConstants.NOTIFICATION_TOPIC_ACCEPTED;

@Slf4j
@Component
public class NotificationListener {

    private final ObjectMapper mapper;
    private final ContactPersistence contactPersistence;
    private final NotificationPersistance notificationPersistance;
    private final UserService userService;


    @Autowired
    public NotificationListener(ObjectMapper mapper,
                                ContactPersistence contactPersistence,
                                NotificationPersistance notificationPersistance,
                                UserService userService) {
        this.mapper = mapper;
        this.contactPersistence = contactPersistence;
        this.notificationPersistance = notificationPersistance;
        this.userService = userService;
    }

    @KafkaListener(topics = NOTIFICATION_TOPIC, groupId = "notificatioId")
    public void saveNotification(String message){
        NotificationInboundMessage msg = mapMessage(message);
        NotificationInboundPayload payload = msg.getPayload();
        var userId = msg.getPayload().getSenderUserId();
        if(payload.getReceiverUserId().equals(userId)){
            Notification not = map(payload);
            notificationPersistance.addNotificaion(not);
        }
    }

    @KafkaListener(topics = NOTIFICATION_TOPIC_ACCEPTED, groupId = "notificatioId")
    public void addContacts(String message) {
        NotificationInboundMessage msg = mapMessage(message);
        log.info("Message incoming {}", message);
        contactPersistence.saveContact(ContactMapper.mapContact(msg.getPayload()));
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
