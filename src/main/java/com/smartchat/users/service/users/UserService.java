package com.smartchat.users.service.users;

import com.smartchat.users.events.users.UserProducer;
import com.smartchat.users.exceptions.ExistingUserException;
import com.smartchat.users.exceptions.UserNotFoundException;
import com.smartchat.users.exceptions.UsernameNotFoundException;
import com.smartchat.users.mapper.UserMapper;
import com.smartchat.users.model.Contacts;
import com.smartchat.users.model.User;
import com.smartchat.users.model.UserPublic;
import com.smartchat.users.persistance.user.ContactPersistence;
import com.smartchat.users.persistance.user.UserPersistance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.smartchat.users.utils.Utils.getUserId;

@Component
@Slf4j
public class UserService {

    private final UserPersistance userPersistance;
    private final ContactPersistence contactPersistence;

    private final UserProducer producer;

    @Autowired
    public UserService(UserPersistance userPersistance, ContactPersistence contactPersistence, UserProducer producer) {
        this.userPersistance = userPersistance;
        this.contactPersistence = contactPersistence;
        this.producer = producer;
    }

    public void createNewUser(User user) throws UsernameNotFoundException, ExistingUserException {
        var username = extractUsername(user);
        if (checkUserExistance(username)) {
            throw new ExistingUserException();
        }
        producer.pushCreateNewUserEvent(user);
    }

    public User retrieveCurrentUserInformations() throws UserNotFoundException {
        var userId = getUserId();
        log.info("Retrieving user informations for userid {}", userId);
        var contactsList = getListContacts(userId);

        if(userId.isBlank()){
            throw new RuntimeException("Username must be populated");
        }
        User user = userPersistance
                .getCurrentUserInformationFromUserId(userId)
                .orElseThrow(UserNotFoundException::new);

        user.setContacts(contactsList);
        return user;
    }


    public UserPublic searchUser(String username) throws UserNotFoundException {
        return userPersistance.searchUserByUsername(username)
                .map(UserMapper::mapDocument)
                .map(UserMapper::maUserPublic)
                .orElseThrow(UserNotFoundException::new);
    }

    public void updateUser(User user) throws UserNotFoundException {
        var userId = getUserId();
        try {
            userPersistance.getCurrentUserInformationFromUserId(userId)
                    .orElseThrow();
            userPersistance.updateUser(userId, UserMapper.mapResponse(user));
        } catch (RuntimeException e) {
            throw new UserNotFoundException();
        }
    }

    private List<Contacts> getListContacts(String userId) {
        return contactPersistence.getContactsByAssociateUsId(userId)
                .stream()
                .map(UserMapper::mapContact)
                .toList();
    }

    private Boolean checkUserExistance(String username) {
        return userPersistance.checkUserExistence(username);
    }

    private String extractUsername(User user) throws UsernameNotFoundException {
        return Optional.of(user.getUsername())
                .orElseThrow(UsernameNotFoundException::new);
    }

}
