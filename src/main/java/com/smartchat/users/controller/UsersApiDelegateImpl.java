package com.smartchat.users.controller;

import com.smartchat.users.api.UsersApiDelegate;
import com.smartchat.users.model.PresignResponse;
import com.smartchat.users.model.User;
import com.smartchat.users.model.UserPublic;
import com.smartchat.users.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UsersApiDelegateImpl implements UsersApiDelegate {

    @Autowired
    private UserService service;

    @Override
    public ResponseEntity<PresignResponse> createNewUser(User user) {
        service.createNewUser(user);
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<User> getMe(String userId) {
        User user = service.getMySelf(userId);
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<UserPublic> getPublicUser(String id){
        UserPublic user = service.searchUser(id);
        return ResponseEntity.ok(user);
    }
}
