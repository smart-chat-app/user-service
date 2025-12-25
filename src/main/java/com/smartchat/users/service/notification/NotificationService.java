package com.smartchat.users.service.notification;

import com.smartchat.users.dto.notification.NotificationDTO;
import com.smartchat.users.events.notification.SendNotificationProducer;
import com.smartchat.users.mapper.NotificationMapper;
import com.smartchat.users.model.Notification;
import com.smartchat.users.persistance.notifications.NotificationPersistance;
import com.smartchat.users.persistance.user.UserPersistance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;


@Slf4j
@Component
public class NotificationService {

    private final SendNotificationProducer producer;
    private final UserPersistance userPersistance;
    private final NotificationPersistance notificationPersistance;

    @Autowired
    public NotificationService(SendNotificationProducer producer, UserPersistance userPersistance, NotificationPersistance notificationPersistance) {
        this.producer = producer;
        this.userPersistance = userPersistance;
        this.notificationPersistance = notificationPersistance;
    }

    public void sendNotification(Notification notification){
        //Also, verify that the sender username is associated with sender user id
        if(notification.getReceiverUsername().isBlank()){
            throw new RuntimeException("username cannot be empty");
        }
        userPersistance.getUser(notification.getReceiverUsername())
                .orElseThrow(() -> new RuntimeException("No user found by this username"));

        log.info("Sending notification");
        producer.pushNotification(notification);
    }

    public List<Notification> retrieveUserNotification(String userId){
        if(Objects.isNull(userId) || userId.isBlank()){
            throw new RuntimeException("UserId is mandatory");
        }
        return notificationPersistance.retrieveNotificationByUserId(userId)
                .stream()
                .map(NotificationMapper::mapDTO)
                .toList();
    }
}
