package com.smartchat.users.repository;

import com.smartchat.users.persistance.model.Contact;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ContactRepository extends ReactiveMongoRepository<Contact, String> {


    @Aggregation(pipeline = {
            "{ $match: { associatUsId: ?0 } }"
    })
    Flux<Contact> findContactsByAssociatUsId(String userId);
}
