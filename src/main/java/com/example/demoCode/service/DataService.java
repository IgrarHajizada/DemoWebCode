package com.example.demoCode.service;

import com.example.demoCode.entity.*;
import com.example.demoCode.repository.ContactRepository;
import com.example.demoCode.repository.GPSInfoRepository;
import com.example.demoCode.repository.ImageRepository;
import com.example.demoCode.repository.RecordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
@Service
public class DataService {
    private final ContactRepository contactRepository;

    private final ImageRepository imageRepository;

    private final GPSInfoRepository gpsInfoRepository;

    private final RecordRepository recordRepository;



    public void saveDataFirstJson(DataInfo data) {
        List<ReqContact> reqContacts = data.getContacts();

        List<Contacts> contacts = convert(reqContacts);
        contactRepository.saveAll(contacts);

        List<Images> images = data.getImages();
        imageRepository.saveAll(images);

        List<GPSInfo> gpsInfos = data.getGps_info();
        gpsInfoRepository.saveAll(gpsInfos);

        List<Records> records = data.getRecords();
        recordRepository.saveAll(records);
    }


    private List<Contacts> convert(List<ReqContact> reqContacts) {
        List<Contacts> contacts = new ArrayList<>();
        for (ReqContact reqContact : reqContacts) {

            Contacts contact = Contacts.builder()
                    .id(reqContact.getId())
                    .name(reqContact.getName())
                    .time_Modified(reqContact.getTime_modified())
                    .numbers(reqContact.getNumbers().toString())
                    .build();
            contacts.add(contact);
        }
        return contacts;

    }

     }


