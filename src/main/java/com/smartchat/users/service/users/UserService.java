package com.smartchat.users.service.users;

import com.smartchat.users.events.users.CreateUserProducer;
import com.smartchat.users.mapper.UserMapper;
import com.smartchat.users.model.Contacts;
import com.smartchat.users.model.User;
import com.smartchat.users.model.UserPublic;
import com.smartchat.users.persistance.user.ContactPersistance;
import com.smartchat.users.persistance.user.UserPersistance;
import com.smartchat.users.persistance.user.model.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static com.smartchat.users.utils.Utils.getUserId;

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
        if(username.isEmpty()){
            log.warn("empty username");
            throw new RuntimeException("Empty username");
        }
        Users users = userPersistance.getUser(username);

        if(Objects.nonNull(users)){
            throw new RuntimeException("User existing");
        } else {
            producer.pushCreateNewUserEvent(user);
        }
    }

    public User getMySelf(){
        String userId = getUserId();
        List<Contacts> contactsList = getListContacts(userId);
        User user = userPersistance.getMyselfFromUserId(userId);
        user.setContacts(contactsList);
        return user;
    }

    public UserPublic searchUser(String username){
        UserPublic user =  userPersistance.searchUserByUsername(username)
                .map(UserMapper::mapDocument)
                .map(UserMapper::maUserPublic)
                .block(Duration.ofSeconds(3));

        if(Objects.isNull(user)){
            throw new RuntimeException("This user doesn't exists");
        }
        return user;
    }

    public void updateUser(User user){
        String userId = getUserId();
        User userToUpdate = userPersistance.getMyselfFromUserId(userId);
        if(Objects.isNull(userToUpdate)){
            throw new RuntimeException("Impossible to update the user because non existant");
        }
        userPersistance.updateUser(userId, UserMapper.mapResponse(user));
    }

    private List<Contacts> getListContacts(String userId){
        return contactPersistance.getContactsByAssociateUsId(userId)
                .stream()
                .map(UserMapper::mapContact)
                .toList();
    }

}
