package com.example.demoCode.service;

import com.example.demoCode.entity.Records;
import com.example.demoCode.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecordService {
    @Autowired
    private RecordRepository recordRepository;


    public List<byte[]> getAllRecords(String directoryPath) throws IOException {
        List<byte[]> recordsData = new ArrayList<>();
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((_, name) -> name.endsWith(".mp3"));

            if (files != null) {
                for (File file : files) {
                    byte[] fileData = Files.readAllBytes(file.toPath());
                    recordsData.add(fileData);
                }
            }
        }
        return recordsData;
    }


    public List<String> getAllRecordNames(String directoryPath) {
        List<String> recordNames = new ArrayList<>();
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".mp3"));

            if (files != null) {
                for (File file : files) {
                    recordNames.add(file.getName());
                }
            }
        }

        return recordNames;
    }

    
    public byte[] getRecordByName(String directoryPath, String fileName) throws IOException {
        File file = new File(directoryPath, fileName);

        if (file.exists() && file.isFile()) {
            return Files.readAllBytes(file.toPath());
        }

        return null;
    }



    public byte[] getRecordById(Long id, String serverRecordPath) throws IOException {
        Records records = recordRepository.findById(id).orElse(null);

        if (records != null) {
            File file = new File(serverRecordPath + "/" + records.getFile());
            FileInputStream fis = new FileInputStream(file);
            byte[] data = fis.readAllBytes();
            fis.close();
            return data;
        }
        return null;
    }
}
