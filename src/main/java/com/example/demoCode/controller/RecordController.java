package com.example.demoCode.controller;

import com.example.demoCode.repository.RecordRepository;
import com.example.demoCode.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/record")
@RequiredArgsConstructor


public class RecordController {

    @Autowired
    private RecordService recordService;
    @Autowired
    private final RecordRepository recordRepository;



    @GetMapping("/AllRecords")
    public ResponseEntity<?> getAllRecords() {
        try {
            List<byte[]> recordsData = recordService.getAllRecords
                    ("src\\main\\resources\\static\\records");

            if (recordsData != null && !recordsData.isEmpty()) {
                List<Resource> resources = new ArrayList<>();
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, "audio/mpeg");

                for (byte[] recordData : recordsData) {
                    resources.add(new ByteArrayResource(recordData));
                }
                return ResponseEntity.ok().headers(headers).body(resources);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }
    }



    @GetMapping("/RecordList")
    public ResponseEntity<List<String>> getAllRecordNames() {
        List<String> recordNames = recordService.getAllRecordNames("src/main/resources/static/records");

        if (recordNames != null && !recordNames.isEmpty()) {
            return ResponseEntity.ok(recordNames);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @GetMapping("/File/{fileName}")
    public ResponseEntity<?> getRecordByName(@PathVariable String fileName) {
        try {
            byte[] recordData = recordService.getRecordByName("src/main/resources/static/records", fileName);

            return getResponseEntity(recordData);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error: " + e.getMessage());
        }
    }



    @GetMapping("/RecordById/{id}")
    public ResponseEntity<?> getRecordById(@PathVariable Long id) {

        try {
            byte[] recordData = recordService.getRecordById
                    (id, "src\\main\\resources\\static\\records");

            return getResponseEntity(recordData);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }



    private ResponseEntity<?> getResponseEntity(byte[] recordData) {
        if (recordData != null) {
            var resource = new ByteArrayResource(recordData);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "audio/mpeg");
            return ResponseEntity.ok().headers(headers).body(resource);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found");
        }
    }
}


