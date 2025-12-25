package com.smartchat.users.persistance.user;

import com.smartchat.users.mapper.UserMapper;
import com.smartchat.users.model.User;
import com.smartchat.users.persistance.user.model.Users;
import com.smartchat.users.persistance.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserPersistance {

    private final UserRepository userRepository;

    @Autowired
    public UserPersistance(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Boolean checkUserExistence(String username){
        return userRepository.isUserExisting(username);
    }

    public Optional<Users> getUser(String username) {
        return userRepository.findByUsernameOrDisplayName(username);
    }

    public void createUser(Users users) {
        userRepository.saveUser(users);
    }

    public Optional<User> getCurrentUserInformationFromUserId(String userId) {
        return userRepository.findByUserId(userId).flatMap(entity -> Optional.ofNullable(UserMapper.mapDocument(entity)));
    }

    public Optional<Users> searchUserByUsername(String username) {
        return userRepository.findByUsernameOrDisplayName(username);
    }

    public void updateUser(String userId, Users users){
        userRepository.updateUser(userId, users);
    }
}
