package com.smartchat.users.persistance.notifications;

import com.smartchat.users.persistance.notifications.model.Notification;
import com.smartchat.users.persistance.notifications.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
public class NotificationPersistance {

    private final NotificationRepository repository;

    @Autowired
    public NotificationPersistance(NotificationRepository repository) {
        this.repository = repository;
    }

    public void addNotificaion(Notification notification){
        repository.save(notification).block();
    }

    public void removeNotification(String notificationId){
        repository.deleteById(notificationId).block();
    }
    public List<Notification> retrieveNotificationByUserId(String userId){
        return repository.findByUserId(userId).blockFirst(Duration.ofSeconds(3));
    }
}
