package com.smartchat.users.service.notification;

import com.smartchat.users.events.notification.SendNotificationProducer;
import com.smartchat.users.model.Notification;
import com.smartchat.users.persistance.user.UserPersistance;
import com.smartchat.users.persistance.user.model.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class NotificationService {

    private final SendNotificationProducer producer;
    private final UserPersistance userPersistance;

    @Autowired
    public NotificationService(SendNotificationProducer producer, UserPersistance userPersistance) {
        this.producer = producer;
        this.userPersistance = userPersistance;
    }

    public void sendNotification(Notification notification){
        //TODO Here i don't need whole notification, i need only receiver username
        //Also, verify that the sender username is associated with sender user id
        Users user = userPersistance.getUser(notification.getReceiverUsername());
        if(Objects.isNull(user)){
            throw new RuntimeException("No user found by this username");
        }
        log.info("Sending notification");
        producer.pushNotification(notification);
    }
}
