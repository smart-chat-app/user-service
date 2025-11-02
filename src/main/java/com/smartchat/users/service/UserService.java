package com.smartchat.users.service;

import com.smartchat.users.events.CreateUserProducer;
import com.smartchat.users.mapper.UserMapper;
import com.smartchat.users.model.Contacts;
import com.smartchat.users.model.User;
import com.smartchat.users.model.UserPublic;
import com.smartchat.users.persistance.ContactPersistance;
import com.smartchat.users.persistance.UserPersistance;
import com.smartchat.users.persistance.model.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class UserService {

    private final UserPersistance userPersistance;
    private final ContactPersistance contactPersistance;

    private final CreateUserProducer producer;

    @Autowired
    public UserService(UserPersistance userPersistance, CreateUserProducer producer, ContactPersistance contactPersistance) {
        this.userPersistance = userPersistance;
        this.producer = producer;
        this.contactPersistance = contactPersistance;
    }

    public void createNewUser(User user){
        String username = user.getUsername();
        Users users = userPersistance.getUser(username);

        if(Objects.nonNull(users)){
            throw new RuntimeException("User existing");
        } else {
            producer.pushCreateNewUserEvent(user);
        }
    }

    public User getMySelf(String userId){
        List<Contacts> contactsList = getListContacts(userId);
        User user = userPersistance.getMyselfFromUserId(userId);
        user.setContacts(contactsList);
        return user;
    }

    public UserPublic searchUser(String username){
        UserPublic user =  userPersistance.searchUserByUsername(username)
                .map(UserMapper::mapDocument)
                .map(UserMapper::maUserPublic)
                .block();

        if(Objects.isNull(user)){
            throw new RuntimeException("This user doesn't exists");
        }
        return user;
    }

    public User updateUser(String userId, User user){
        User userToUpdate = userPersistance.getMyselfFromUserId(userId);
        if(Objects.isNull(userToUpdate)){
            throw new RuntimeException("Impossible to update the user because non existant");
        }
        userPersistance.createOrUpdateUser(UserMapper.mapResponse(user));
        return user;
    }

    private List<Contacts> getListContacts(String userId){
        return contactPersistance.getContactsByAssociateUsId(userId)
                .stream()
                .map(UserMapper::mapContact)
                .toList();
    }

}
