package com.smartchat.users.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartchat.users.mapper.UsersToUserEntityMapper;
import com.smartchat.users.message.model.UserMessage;
import com.smartchat.users.message.model.UserMessageOutbound;
import com.smartchat.users.persistance.UserPersistance;
import com.smartchat.users.persistance.model.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CreateUserListener {

    private UserPersistance persistance;
    private ObjectMapper mapper;
    private static final String TOPIC = "user.created";

    @Autowired
    public CreateUserListener(ObjectMapper mapper, UserPersistance persistance) {
        this.mapper = mapper;
        this.persistance = persistance;
    }

    @KafkaListener(topics = TOPIC, groupId = "userCreationId")
    public void createNewUser(String message){
        UserMessageOutbound user = mapMessage(message);
        log.info("Creating user with userId {}", user.getPayload().getUserId());
        UserEntity userEntity = UsersToUserEntityMapper.mapFromKafka(user.getPayload());
        persistance.createUser(userEntity);
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
