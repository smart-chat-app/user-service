package com.smartchat.users.service;

import com.smartchat.users.events.CreateUserProducer;
import com.smartchat.users.model.User;
import com.smartchat.users.persistance.UserPersistance;
import com.smartchat.users.persistance.model.UserEntity;
import com.smartchat.users.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class UserService {

    @Autowired
    private UserPersistance userPersistance;
    @Autowired
    private CreateUserProducer producer;

    public void createNewUser(User user){
        String username = user.getUsername();
        UserEntity userEntity = userPersistance.getUser(username);

        if(Objects.nonNull(userEntity)){
            throw new RuntimeException("User existing");
        } else {
            producer.pushCreateNewUserEvent(user);
        }
    }

    public User getMySelf(String token){
        String userId = Utils.getUserIdFromToken(token);
        return userPersistance.getMyselfFromUserId(userId);
    }
}
