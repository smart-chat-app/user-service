package com.smartchat.users.service.notification;

import com.smartchat.users.events.notification.SendNotificationProducer;
import com.smartchat.users.model.Notification;
import com.smartchat.users.persistance.notifications.NotificationPersistance;
import com.smartchat.users.persistance.notifications.model.NotificationEntity;
import com.smartchat.users.persistance.user.UserPersistance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private SendNotificationProducer producer;

    @Mock
    private UserPersistance userPersistance;

    @Mock
    private NotificationPersistance notificationPersistance;

    @InjectMocks
    private NotificationService service;

    @Test
    void sendNotificationRejectsBlankReceiverUsername() {
        Notification notification = new Notification();
        notification.setReceiverUsername(" ");

        assertThatThrownBy(() -> service.sendNotification(notification))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("username cannot be empty");
    }

    @Test
    void sendNotificationRejectsMissingUser() {
        Notification notification = new Notification();
        notification.setReceiverUsername("target");

        when(userPersistance.getUser("target")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.sendNotification(notification))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No user found by this username");
    }

    @Test
    void retrieveUserNotificationRequiresUserId() {
        assertThatThrownBy(() -> service.retrieveUserNotification(" "))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("UserId is mandatory");
    }

    @Test
    void retrieveUserNotificationMapsEntities() {
        NotificationEntity entity = NotificationEntity.builder()
                .notificationId(UUID.randomUUID().toString())
                .userId(UUID.randomUUID().toString())
                .sender("alex")
                .build();

        when(notificationPersistance.retrieveNotificationByUserId(entity.getUserId()))
                .thenReturn(List.of(entity));

        List<Notification> result = service.retrieveUserNotification(entity.getUserId());

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getSenderUsername()).isEqualTo("alex");
    }
}
