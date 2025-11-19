package com.smartchat.users.rest.users;

import com.smartchat.users.api.UsersApiDelegate;
import com.smartchat.users.model.PresignResponse;
import com.smartchat.users.model.User;
import com.smartchat.users.model.UserPublic;
import com.smartchat.users.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UsersApiDelegateImpl implements UsersApiDelegate {


    private final UserService service;

    @Autowired
    public UsersApiDelegateImpl(UserService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<PresignResponse> createNewUser(User user) {
        service.createNewUser(user);
        return ResponseEntity.ok(PresignResponse.builder()
                        .method(HttpStatus.CREATED.name())
                .build());
    }

    @Override
    public ResponseEntity<User> getMe(String userId) {
        User user = service.getMySelf(userId);
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<UserPublic> getPublicUser(String id) {
        UserPublic user = service.searchUser(id);
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<PresignResponse> updateUser(String userId, User user) {
        service.updateUser(userId, user);
        return ResponseEntity.ok(PresignResponse.builder()
                .method(HttpStatus.CREATED.name())
                .build());
    }
}
