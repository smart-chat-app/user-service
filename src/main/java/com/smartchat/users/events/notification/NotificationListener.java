package com.smartchat.users.events.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartchat.users.mapper.ContactMapper;
import com.smartchat.users.message.model.NotificationInboundMessage;
import com.smartchat.users.message.model.NotificationInboundPayload;
import com.smartchat.users.persistance.notifications.NotificationPersistance;
import com.smartchat.users.persistance.user.ContactPersistance;
import com.smartchat.users.service.users.UserService;
import com.smartchat.users.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.smartchat.users.utils.ContextConstants.NOTIFICATION_TOPIC;
import static com.smartchat.users.utils.ContextConstants.NOTIFICATION_TOPIC_ACCEPTED;

@Slf4j
@Component
public class NotificationListener {

    private final ObjectMapper mapper;
    private final ContactPersistance contactPersistance;
    private final NotificationPersistance notificationPersistance;
    private final UserService userService;


    @Autowired
    public NotificationListener(ObjectMapper mapper,
                                ContactPersistance contactPersistance,
                                NotificationPersistance notificationPersistance,
                                UserService userService) {
        this.mapper = mapper;
        this.contactPersistance = contactPersistance;
        this.notificationPersistance = notificationPersistance;
        this.userService = userService;
    }

    @KafkaListener(topics = NOTIFICATION_TOPIC, groupId = "notificatioId")
    public void saveNotification(String message){
        NotificationInboundMessage msg = mapMessage(message);
        NotificationInboundPayload payload = msg.getPayload();
        String userId = Utils.getUserId();
        if(payload.getReceiverUserId().equals(userId)){
            //TODO Add mapping for notification
            notificationPersistance.addNotificaion(null);
        }
    }

    @KafkaListener(topics = NOTIFICATION_TOPIC_ACCEPTED, groupId = "notificatioId")
    public void addContacts(String message) {
        NotificationInboundMessage msg = mapMessage(message);
        log.info("Message incoming {}", message);
        contactPersistance.saveContact(ContactMapper.mapContact(msg.getPayload()));
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
