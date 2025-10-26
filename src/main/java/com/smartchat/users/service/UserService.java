package com.smartchat.users.service;

import com.smartchat.users.persistance.UserPersistance;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class UserService {

    private UserPersistance userPersistance;


}
