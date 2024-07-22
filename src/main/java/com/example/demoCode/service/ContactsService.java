package com.example.demoCode.service;

import com.example.demoCode.entity.Contacts;
import com.example.demoCode.entity.GPSInfo;
import com.example.demoCode.entity.Images;
import com.example.demoCode.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactsService {
    private final ContactRepository contactRepository;

    public List<Contacts> getAllContactsInfo() {
        return contactRepository.findAll();
    }

    public List<Contacts> getRandomContacts() {
        return contactRepository.findRandomContacts();
    }

}
