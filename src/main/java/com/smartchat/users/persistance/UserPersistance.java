package com.smartchat.users.persistance;

import com.smartchat.users.mapper.UsersToUserEntityMapper;
import com.smartchat.users.model.User;
import com.smartchat.users.persistance.model.UserEntity;
import com.smartchat.users.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class UserPersistance {

    @Autowired
    private UserRepository userRepository;

    public UserEntity getUser(String username){
        return userRepository.findByUsername(username).block();
    }

    public void createUser(User user){
        UserEntity userEntity = UsersToUserEntityMapper.map(user);
        userRepository.save(userEntity).block(Duration.ofSeconds(3));
    }
}
