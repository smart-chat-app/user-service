package com.smartchat.users.rest.notification;

import com.smartchat.users.api.NotificationApiDelegate;
import com.smartchat.users.model.Notification;
import com.smartchat.users.model.PresignResponse;
import com.smartchat.users.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class NotificationDelegateImpl implements NotificationApiDelegate {

    private final NotificationService notificationService;

    @Autowired
    public NotificationDelegateImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public ResponseEntity<PresignResponse> sendContactNotification(String senderuuId,
                                                                   Notification notification){
        notification.setUserId(senderuuId);
        notificationService.sendNotification(notification);
        return ResponseEntity.ok(PresignResponse.builder()
                        .method(HttpStatus.OK.name())
                .build());
    }
}
