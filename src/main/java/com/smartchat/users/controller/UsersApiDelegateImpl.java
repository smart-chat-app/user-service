package com.smartchat.users.controller;

import com.smartchat.users.api.UsersApiDelegate;
import com.smartchat.users.model.PresignResponse;
import com.smartchat.users.model.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UsersApiDelegateImpl implements UsersApiDelegate {

    @Override
    public ResponseEntity<PresignResponse> createNewUser(User user){
        return ResponseEntity.ok(null);
    }
}
