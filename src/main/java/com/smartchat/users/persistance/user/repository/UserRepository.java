package com.smartchat.users.persistance.user.repository;

import com.smartchat.users.persistance.user.model.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Repository
public class UserRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public UserRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Optional<Users> findByUserId(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        return Optional.ofNullable(mongoTemplate.findOne(query, Users.class));
    }

    public Optional<Users> findByUsernameOrDisplayName(String value) {
        Query query = new Query(new Criteria().orOperator(
                Criteria.where("username").is(value),
                Criteria.where("displayName").is(value)
        ));
        return Optional.ofNullable(mongoTemplate.findOne(query, Users.class));
    }

    public Boolean isUserExisting(String value) {
        Query query = new Query(new Criteria().orOperator(
                Criteria.where("username").is(value),
                Criteria.where("displayName").is(value)
        ));
        return mongoTemplate.exists(query, Users.class);
    }

    public void updateUser(String userId, Users user) {
        log.info("User: {}", user.getUserIdKey().getUserId());
        Query query = new Query(Criteria.where("userId").is(userId));
        Update update = new Update();
        update.set("username", user.getUserIdKey().getUsername());
        update.set("displayName", user.getDisplayName());
        update.set("bio", user.getBio());
        update.set("avatarUrl", user.getAvatarUrl());

        mongoTemplate.findAndModify(query, update,
                FindAndModifyOptions.options().returnNew(true).upsert(false),
                Users.class
        );
    }

    public void saveUser(Users user) {
        mongoTemplate.save(user);
    }
}
