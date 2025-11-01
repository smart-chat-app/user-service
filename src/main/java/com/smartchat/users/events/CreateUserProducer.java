package com.smartchat.users.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartchat.users.message.model.UserMessage;
import com.smartchat.users.message.model.UserMessageHeader;
import com.smartchat.users.message.model.UserMessagePayload;
import com.smartchat.users.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Objects;

@Slf4j
@Component
public class CreateUserProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final static String TOPIC = "user.created";

    private final ObjectMapper mapper;

    @Autowired
    public CreateUserProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper mapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.mapper = mapper;
    }


    public void pushCreateNewUserEvent(User user) {
        Objects.requireNonNull(user, "user must not be null");

        String json = mapMessage(user);
        log.info("json {}", json);
        Message<String> msg = MessageBuilder
                .withPayload(json)
                .setHeader(KafkaHeaders.KEY, user.getUserId())
                .setHeader(org.springframework.messaging.MessageHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        try {
            log.info("Sending message for userId {}", user.getUserId());
            kafkaTemplate.send(TOPIC, json);
        } catch (Exception e) {
            log.error("Problem to send the message {}", e.getMessage());
        }
    }

    private String mapMessage(User user) {
        UserMessage message = UserMessage.builder()
                .header(UserMessageHeader.builder()
                        .userId(user.getUserId())
                        .build())
                .payload(UserMessagePayload.builder()
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .displayName(user.getDisplayName())
                        .bio(user.getBio() != null && user.getBio().isPresent() ? user.getBio().get() : null)
                        .avatarUrl(URI.create(user.getAvatarUrl() != null && user.getAvatarUrl().isPresent()
                                ? user.getAvatarUrl().get().toString() : null))
                       /* .createdAt(OffsetDateTime.now())
                        .updatedAt(OffsetDateTime.now())*/
                        .build())
                .build();
        log.info("msg {}", message);
        try {
            return mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            log.error("Impossible to parse {}", e.getMessage());
            return null;
        }
    }
}
