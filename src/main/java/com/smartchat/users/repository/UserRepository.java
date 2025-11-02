package com.smartchat.users.repository;

import com.smartchat.users.persistance.model.Users;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<Users, String> {
    Mono<Users> findByUserId(String userId);

    @Aggregation(pipeline = {
            "{ $match: { $or: [ {username: ?0 }, {displayName: ?0} ] } }"
    })
    Mono<Users> findByUsernameOrDisplayName(String username);
}
