package com.smartchat.users.service.users;

import com.smartchat.users.events.users.UserProducer;
import com.smartchat.users.exceptions.ExistingUserException;
import com.smartchat.users.exceptions.UserNotFoundException;
import com.smartchat.users.exceptions.UsernameNotFoundException;
import com.smartchat.users.model.Contacts;
import com.smartchat.users.model.User;
import com.smartchat.users.persistance.user.ContactPersistence;
import com.smartchat.users.persistance.user.UserPersistance;
import com.smartchat.users.persistance.user.model.Contact;
import com.smartchat.users.utils.ContextConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserPersistance userPersistance;

    @Mock
    private ContactPersistence contactPersistence;

    @Mock
    private UserProducer producer;

    @InjectMocks
    private UserService service;

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void createNewUserPublishesEventWhenUserDoesNotExist() throws UsernameNotFoundException, ExistingUserException {
        User user = new User();
        user.setUsername("alex");

        when(userPersistance.checkUserExistence("alex")).thenReturn(false);

        service.createNewUser(user);

        verify(producer).pushCreateNewUserEvent(user);
    }

    @Test
    void createNewUserThrowsWhenUserAlreadyExists() throws ExistingUserException, UsernameNotFoundException {
        User user = new User();
        user.setUsername("alex");
        service.createNewUser(user);
        when(userPersistance.checkUserExistence("alex")).thenReturn(true);

        assertThatThrownBy(() -> service.createNewUser(user))
                .isInstanceOf(ExistingUserException.class);
    }

    @Test
    void retrieveCurrentUserInformationsAddsContactsFromPersistence() throws UserNotFoundException {
        String userId = UUID.randomUUID().toString();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(ContextConstants.USER_ID, userId);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        User user = new User();
        user.setUserId(userId);
        user.setUsername("alex");
        user.setDisplayName("Alex");

        Contact contact = Contact.builder()
                .userId(UUID.randomUUID().toString())
                .username("sam")
                .bio("bio")
                .build();

        when(contactPersistence.getContactsByAssociateUsId(anyString())).thenReturn(List.of(contact));
        when(userPersistance.getCurrentUserInformationFromUserId(userId)).thenReturn(Optional.of(user));

        User result = service.retrieveCurrentUserInformations();

        assertThat(result.getContacts()).hasSize(1);
        Contacts mappedContact = result.getContacts().getFirst();
        assertThat(mappedContact.getUsername()).isEqualTo("sam");
    }
}
