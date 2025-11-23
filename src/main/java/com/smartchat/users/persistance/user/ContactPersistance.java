package com.smartchat.users.persistance.user;

import com.smartchat.users.persistance.user.model.Contact;
import com.smartchat.users.persistance.user.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class ContactPersistance {
    private final ContactRepository repository;

    @Autowired
    public ContactPersistance(ContactRepository repository) {
        this.repository = repository;
    }

    public List<Contact> getContactsByAssociateUsId(String userId){
        return repository.findContactsByAssociatUsId(userId).collectList().block();
    }

    public void saveContact(Contact contact){
        Contact con = repository.findByNotificationId(contact.getNotificationId()).next().block();
        if(Objects.isNull(con)){
            log.info("Saving contact for userId: {}", contact.getUserId());
            repository.save(contact).block();
        }
    }
}
