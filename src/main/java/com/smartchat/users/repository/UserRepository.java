package com.smartchat.users.repository;

import com.smartchat.users.persistance.model.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class UserRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    @Autowired
    public UserRepository(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Mono<Users> findByUserId(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        return mongoTemplate.findOne(query, Users.class);
    }

    public Mono<Users> findByUsernameOrDisplayName(String value) {
        Query query = new Query(new Criteria().orOperator(
                Criteria.where("username").is(value),
                Criteria.where("displayName").is(value)
        ));
        return mongoTemplate.findOne(query, Users.class);
    }

    public Mono<Users> updateUser(String userId, Users user) {
        log.info("User: {}", user.getUserId());
        Query query = new Query(Criteria.where("userId").is(userId));
        Update update = new Update();
        update.set("username", user.getUsername());
        update.set("displayName", user.getDisplayName());
        update.set("bio", user.getBio());
        update.set("avatarUrl", user.getAvatarUrl());

        return mongoTemplate.findAndModify(query, update,
                FindAndModifyOptions.options().returnNew(true).upsert(false),
                Users.class
        );
    }

    public Mono<Users> saveUser(Users user) {
        return mongoTemplate.save(user);
    }
}
