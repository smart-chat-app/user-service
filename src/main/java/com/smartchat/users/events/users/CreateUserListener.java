package com.smartchat.users.events.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartchat.users.mapper.UserMapper;
import com.smartchat.users.message.model.UserMessageOutbound;
import com.smartchat.users.persistance.user.UserPersistance;
import com.smartchat.users.persistance.user.model.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CreateUserListener {

    private final UserPersistance persistance;
    private final ObjectMapper mapper;
    private static final String TOPIC = "user.created";

    @Autowired
    public CreateUserListener(UserPersistance persistance, ObjectMapper mapper) {
        this.persistance = persistance;
        this.mapper = mapper;
    }

    @KafkaListener(topics = TOPIC, groupId = "userCreationId")
    public void createNewUser(String message){
        UserMessageOutbound user = mapMessage(message);
        log.info("Creating user with userId {}", user.getPayload().getUserId());
        Users users = UserMapper.mapFromKafka(user.getPayload());
        persistance.createUser(users);
    }

    private UserMessageOutbound mapMessage(String message){
        try{
            return mapper.readValue(message, UserMessageOutbound.class);
        } catch (JsonProcessingException e) {
            log.error("Impossible to parse message {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
