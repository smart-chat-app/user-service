package com.smartchat.users.persistance;

import com.smartchat.users.persistance.model.Contact;
import com.smartchat.users.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContactPersistance {
    private ContactRepository repository;

    @Autowired
    public ContactPersistance(ContactRepository repository) {
        this.repository = repository;
    }

    public List<Contact> getContactsByAssociateUsId(String userId){
        return repository.findContactsByAssociatUsId(userId).collectList().block();
    }
}
