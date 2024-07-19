package com.example.demoCode.telegram;

import com.example.demoCode.entity.GPSInfo;
import com.example.demoCode.entity.Images;
import com.example.demoCode.entity.Records;
import com.example.demoCode.service.GpsService;
import com.example.demoCode.service.ImageService;
import com.example.demoCode.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MyTelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;


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



    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equals("/start")) {
                sendMenu(chatId);
            } else if (messageText.equals("Get All Images")) {
                sendAllImages(chatId);
            } else if (messageText.equals("Get All Records")) {
                sendAllAudio(chatId);
            } else if (messageText.equals("Get All Gps Information")) {
                    sendGPS(chatId);
                }
             else {
                sendMessage(chatId, "Invalid command. Please choose an option from the menu.");
            }
        }
    }


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
        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //--Image
    private void sendAllImages(long chatId) {
        List<Images> imagesList = imageService.getAllImageNames();

        List<String> imagePaths = new ArrayList<>();
        for (Images images : imagesList) {
            imagePaths.add("static/images/" + images.getFile());
        }
        for (String imagePath : imagePaths) {

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

    }


    //---Record
    private void sendAllAudio(long chatId) {

        List<Records> recordList = recordService.getAllRecordNames();

        List<String> recordePaths = new ArrayList<>();
        for (Records records : recordList) {
            recordePaths.add("static/records/" + records.getFile());
        }

        for (String recordsPath : recordePaths) {

            SendAudio audio = new SendAudio();
            audio.setChatId(chatId);

            try {
                File file = new ClassPathResource(recordsPath).getFile();
                InputFile inputFile = new InputFile(file);
                audio.setAudio(inputFile);

                execute(audio);
            } catch (IOException | TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }


    //--- GPS
    private void sendGPS(long chatId) {
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
            }
            catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }



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


}
