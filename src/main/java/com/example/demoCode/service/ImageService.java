package com.example.demoCode.service;

import com.example.demoCode.entity.Images;
import com.example.demoCode.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ImageService {

    private final   ImageRepository imageRepository;


    public List<Images> getAllImageNames() {
        return imageRepository.findAll();
    }
}
