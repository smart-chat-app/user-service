package com.smartchat.users.events;

import com.smartchat.users.model.User;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class CreateUserProducer {

    private StreamBridge streamBridge;

    public void pushCreateNewUserEvent(User user){
        //TODO implement the producer for
    }
}
