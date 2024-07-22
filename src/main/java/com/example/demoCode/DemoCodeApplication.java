package com.example.demoCode;


import com.example.demoCode.entity.DataInfo;
import com.example.demoCode.service.DataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
@RequiredArgsConstructor
public class DemoCodeApplication implements CommandLineRunner {

    private final DataService dataService;

    public static void main(String[] args) throws IOException {

        SpringApplication.run(DemoCodeApplication.class, args);
    }

    @Override
    public void run(String... args) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            DataInfo dataInfoJson = objectMapper.readValue(new File
                            ("src\\main\\java\\com\\example\\demoCode\\info.json")
                    , DataInfo.class);
            System.out.println(dataInfoJson);
            dataService.saveDataFirstJson(dataInfoJson);


        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


}
