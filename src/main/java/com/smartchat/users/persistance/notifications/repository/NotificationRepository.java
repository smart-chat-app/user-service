package com.smartchat.users.persistance.notifications.repository;

import com.smartchat.users.persistance.notifications.model.Notification;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface NotificationRepository extends ReactiveMongoRepository<Notification, String> {
    @Aggregation(pipeline = {
            "{ $match: { userId: ?0 } }"
    })
    Flux<List<Notification>> findByUserId(String userId);
}
