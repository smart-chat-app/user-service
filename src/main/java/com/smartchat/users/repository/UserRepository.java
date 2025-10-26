package com.smartchat.users.repository;

import com.smartchat.users.domain.UserDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<UserDocument, String> {
    Mono<UserDocument> findByUserId(String userId);
    Mono<UserDocument> findByUsername(String username);
}
