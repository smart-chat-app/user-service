package com.smartchat.users.persistance;

import com.smartchat.users.mapper.UserMapper;
import com.smartchat.users.model.User;
import com.smartchat.users.persistance.model.Users;
import com.smartchat.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class UserPersistance {

    private final UserRepository userRepository;

    @Autowired
    public UserPersistance(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Users getUser(String username){
        return userRepository.findByUsernameOrDisplayName(username).block();
    }

    public void createOrUpdateUser(Users users){
        userRepository.save(users).block(Duration.ofSeconds(3));
    }

    public User getMyselfFromUserId(String userId){
        return userRepository.findByUserId(userId)
                .map(UserMapper::mapDocument)
                .blockOptional().orElseThrow(() -> new RuntimeException("No user found"));
    }

    public Mono<Users> searchUserByUsername(String username){
        return userRepository.findByUsernameOrDisplayName(username);
    }
}
