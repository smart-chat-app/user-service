package com.smartchat.users.persistance.notifications.repository;

import com.smartchat.users.persistance.notifications.model.NotificationEntity;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<NotificationEntity, String> {
    @Aggregation(pipeline = {
            "{ $match: { userId: ?0 } }"
    })
    List<NotificationEntity> findByUserId(String userId);
}
