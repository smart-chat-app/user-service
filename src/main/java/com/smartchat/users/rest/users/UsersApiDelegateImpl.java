package com.smartchat.users.rest.users;

import com.smartchat.users.api.UsersApiDelegate;
import com.smartchat.users.model.PresignResponse;
import com.smartchat.users.model.User;
import com.smartchat.users.model.UserPublic;
import com.smartchat.users.service.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@Service
public class UsersApiDelegateImpl implements UsersApiDelegate {


    private final UserService service;

    @Autowired
    public UsersApiDelegateImpl(UserService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<PresignResponse> createNewUser(User user) {
        try {
            service.createNewUser(user);
            return ResponseEntity.ok(PresignResponse.builder()
                    .method(HttpStatus.CREATED.name())
                    .build());
        } catch(Exception e){
            return ResponseEntity.badRequest()
                    .body(PresignResponse.builder()
                            .message(e.getMessage())
                            .method(HttpStatus.BAD_REQUEST.name())
                            .build());
        }
    }

    @Override
    public ResponseEntity<User> getMe() {
        try {
            User user = service.getMySelf();
            return ResponseEntity.ok(user);
        }catch(Exception e){
            log.error(Arrays.toString(e.getStackTrace()));
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<UserPublic> getPublicUser(String id) {
        try {
            UserPublic user = service.searchUser(id);
            return ResponseEntity.ok(user);
        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<PresignResponse> updateUser(User user) {
        try {
            service.updateUser(user);
            return ResponseEntity.ok(PresignResponse.builder()
                    .method(HttpStatus.CREATED.name())
                    .build());
        }catch(Exception e){
            return ResponseEntity.badRequest()
                    .body(PresignResponse.builder()
                            .message(e.getMessage())
                            .method(HttpStatus.BAD_REQUEST.name())
                            .build());
        }
    }
}
