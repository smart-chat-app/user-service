package com.smartchat.users.repository;

import com.smartchat.users.persistance.model.UserEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<UserEntity, String> {
    Mono<UserEntity> findByUserId(String userId);
    Mono<UserEntity> findByUsername(String username);
}
