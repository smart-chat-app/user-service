package com.smartchat.users.service;

import com.smartchat.users.model.User;
import com.smartchat.users.persistance.UserPersistance;
import com.smartchat.users.persistance.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
@Slf4j
public class UserService {

    private UserPersistance userPersistance;

    public void createNewUser(User user){
        String username = user.getUsername();
        UserEntity userEntity = userPersistance.getUser(username);

        if(Objects.nonNull(userEntity)){
            throw new RuntimeException("User existing");
        } else {
            userPersistance.createUser(user);
        }
    }
}
