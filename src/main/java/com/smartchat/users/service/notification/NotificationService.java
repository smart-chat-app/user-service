package com.smartchat.users.service.notification;

import com.smartchat.users.events.notification.SendNotificationProducer;
import com.smartchat.users.exceptions.UserNotFoundException;
import com.smartchat.users.exceptions.UsernameNotFoundException;
import com.smartchat.users.mapper.NotificationMapper;
import com.smartchat.users.model.Notification;
import com.smartchat.users.model.User;
import com.smartchat.users.persistance.notifications.NotificationPersistance;
import com.smartchat.users.persistance.user.UserPersistance;
import com.smartchat.users.persistance.user.model.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


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

    public void sendNotification(Notification notification) throws UsernameNotFoundException {

        Users receiver = Optional.ofNullable(notification.getReceiverUsername())
                .map(String::trim)
                .filter(un -> !un.isEmpty())
                .flatMap(userPersistance::getUser)
                .orElseThrow(UsernameNotFoundException::new);

        String receiverUserId = receiver.getUserIdKey().getUserId();
        notification.setReceiverUserId(receiverUserId);
        producer.pushNotification(notification);

        log.info("Notification sent to userId={}", receiverUserId);
    }

    public List<Notification> retrieveUserNotification(String userId) throws UserNotFoundException {
        if(null == userId || userId.isBlank()) throw new UserNotFoundException();
        return notificationPersistance.retrieveNotificationByUserId(userId)
                .stream()
                .map(NotificationMapper::mapDTO)
                .toList();
    }
}
