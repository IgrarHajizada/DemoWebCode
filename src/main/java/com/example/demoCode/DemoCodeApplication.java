package com.example.demoCode;

import com.example.demoCode.entity.DataInfo;
import com.example.demoCode.service.DataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLOutput;

@SpringBootApplication
@RequiredArgsConstructor
public class DemoCodeApplication implements CommandLineRunner {

    private final DataService dataService;

    public static void main(String[] args) throws IOException {

        SpringApplication.run(DemoCodeApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {



       try {
            ObjectMapper objectMapper = new ObjectMapper();
            DataInfo dataInfo = objectMapper.readValue(new File
                            ("src\\main\\java\\com\\example\\demoCode\\info.json")
                    , DataInfo.class);
            System.out.println(dataInfo);

            dataService.saveData(dataInfo);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }
}
