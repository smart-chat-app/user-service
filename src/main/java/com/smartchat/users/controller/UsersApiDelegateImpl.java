package com.smartchat.users.controller;

import com.smartchat.users.api.UsersApiDelegate;
import com.smartchat.users.model.PresignResponse;
import com.smartchat.users.model.User;
import com.smartchat.users.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsersApiDelegateImpl implements UsersApiDelegate {

    private UserService service;

    @Override
    public ResponseEntity<PresignResponse> createNewUser(User user){
        service.createNewUser(user);
        return ResponseEntity.ok(null);
    }
}
