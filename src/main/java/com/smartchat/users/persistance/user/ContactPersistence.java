package com.smartchat.users.persistance.user;

import com.smartchat.users.persistance.user.model.Contact;
import com.smartchat.users.persistance.user.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class ContactPersistence {
    private final ContactRepository repository;

    @Autowired
    public ContactPersistence(ContactRepository repository) {
        this.repository = repository;
    }

    public List<Contact> getContactsByAssociateUsId(String userId) {
        return repository.findContactsByAssociatUsId(userId);
    }

    public void saveContact(Contact contact) {
        repository.findByNotificationId(contact.getNotificationId())
                .map(repository::save)
                .orElseThrow(() -> new RuntimeException("Problem in saving user"));
    }
}
