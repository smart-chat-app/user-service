package com.smartchat.users.persistance;

import com.smartchat.users.mapper.UsersToUserEntityMapper;
import com.smartchat.users.model.User;
import com.smartchat.users.persistance.model.UserEntity;
import com.smartchat.users.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserPersistance {

    private UserRepository userRepository;
    private UsersToUserEntityMapper mapper;

    public UserEntity getUser(String username){
        return userRepository.findByUsername(username).block();
    }

    public void createUser(User user){
        UserEntity userEntity = mapper.map(user);
        userRepository.save(userEntity);
    }
}
