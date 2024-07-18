package com.example.demoCode.service;

import com.example.demoCode.entity.Images;
import com.example.demoCode.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;


    public byte[] getImageById(Long id, String serverImagesPath) throws IOException {
        Images image = imageRepository.findById(id).orElse(null);

        if (image != null) {
            File file = new File(serverImagesPath + "/" + image.getFile());
            FileInputStream fis = new FileInputStream(file);
            byte[] data = fis.readAllBytes();
            fis.close();
            return data;
        }
        return null;
    }


    public List<String> getAllImageNames(String directoryPath) {
        List<String> imageNames = new ArrayList<>();
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((_, name) -> name.endsWith(".jpg"));

            if (files != null) {
                for (File file : files) {
                    imageNames.add(file.getName());
                }
            }
        }
        return imageNames;
    }


    public byte[] getImageByName(String directoryPath, String fileName) throws IOException {
        File file = new File(directoryPath, fileName);

        if (file.exists() && file.isFile()) {
            return Files.readAllBytes(file.toPath());
        }

        return null;
    }

}
