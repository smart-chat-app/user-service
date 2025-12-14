package com.smartchat.users.service.users;

import com.smartchat.users.events.users.CreateUserProducer;
import com.smartchat.users.exceptions.UserNotFoundException;
import com.smartchat.users.exceptions.UsernameNotFoundException;
import com.smartchat.users.mapper.UserMapper;
import com.smartchat.users.model.Contacts;
import com.smartchat.users.model.User;
import com.smartchat.users.model.UserPublic;
import com.smartchat.users.persistance.user.ContactPersistance;
import com.smartchat.users.persistance.user.UserPersistance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static com.smartchat.users.utils.Utils.getUserId;

@Component
@Slf4j
public class UserService {

    private final UserPersistance userPersistance;
    private final ContactPersistance contactPersistance;

    private final CreateUserProducer producer;

    @Autowired
    public UserService(UserPersistance userPersistance, ContactPersistance contactPersistance, CreateUserProducer producer) {
        this.userPersistance = userPersistance;
        this.contactPersistance = contactPersistance;
        this.producer = producer;
    }

    public void createNewUser(User user) throws UsernameNotFoundException {
        var username = extractUsername(user);
        if (checkUserExistance(username)) {
            producer.pushCreateNewUserEvent(user);
        } else {
            throw new RuntimeException("User already exists");
        }
    }

    public User retrieveCurrentUserInformations() throws UserNotFoundException {
        var userId = getUserId();
        var contactsList = getListContacts(userId);
        var user = userPersistance.getCurrentUserInformatiosnFromUserId(userId)
                .blockOptional()
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setContacts(contactsList);
        return user;
    }

    public UserPublic searchUser(String username) throws UserNotFoundException {
        return userPersistance.searchUserByUsername(username)
                .map(UserMapper::mapDocument)
                .map(UserMapper::maUserPublic)
                .blockOptional(Duration.ofSeconds(3)).orElseThrow(() -> new UserNotFoundException("User Not found"));
    }

    public void updateUser(User user) throws UserNotFoundException {
        var userId = getUserId();
        try {
            userPersistance.getCurrentUserInformatiosnFromUserId(userId)
                    .blockOptional()
                    .orElseThrow();
            userPersistance.updateUser(userId, UserMapper.mapResponse(user));
        } catch (RuntimeException e) {
            throw new UserNotFoundException("User not found");
        }
    }

    private List<Contacts> getListContacts(String userId) {
        return contactPersistance.getContactsByAssociateUsId(userId)
                .stream()
                .map(UserMapper::mapContact)
                .toList();
    }

    private Boolean checkUserExistance(String username) {
        return userPersistance.checkUserExistance(username)
                .block();
    }

    private String extractUsername(User user) throws UsernameNotFoundException {
        return Optional.of(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

}
