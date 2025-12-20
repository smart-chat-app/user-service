package com.smartchat.users.persistance.user.repository;

import com.smartchat.users.persistance.user.model.Contact;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends MongoRepository<Contact, String> {


    @Aggregation(pipeline = {
            "{ $match: { associatUsId: ?0 } }"
    })
    List<Contact> findContactsByAssociatUsId(String userId);
    @Aggregation(pipeline = {
            "{ $match: { notificationId: ?0 } }"
    })
    Optional<Contact> findByNotificationId(String notificationId);
}
