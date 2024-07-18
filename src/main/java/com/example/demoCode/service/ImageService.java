package com.example.demoCode.service;

import com.example.demoCode.entity.Images;
import com.example.demoCode.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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

}
