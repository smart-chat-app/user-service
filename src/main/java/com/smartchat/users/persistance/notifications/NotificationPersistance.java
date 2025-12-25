package com.smartchat.users.persistance.notifications;

import com.smartchat.users.persistance.notifications.model.NotificationEntity;
import com.smartchat.users.persistance.notifications.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class NotificationPersistance {

    private final NotificationRepository repository;

    @Autowired
    public NotificationPersistance(NotificationRepository repository) {
        this.repository = repository;
    }

    public void addNotificaion(NotificationEntity notificationEntity){
        repository.save(notificationEntity);
    }

    public void removeNotification(String notificationId){
        repository.deleteById(notificationId);
    }
    public List<NotificationEntity> retrieveNotificationByUserId(String userId){
        return repository.findByUserId(userId);
    }
}
