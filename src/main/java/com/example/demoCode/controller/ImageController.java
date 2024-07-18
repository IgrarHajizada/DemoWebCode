package com.example.demoCode.controller;

import com.example.demoCode.entity.Images;
import com.example.demoCode.repository.ImageRepository;
import com.example.demoCode.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor

public class ImageController {

    @Autowired
    private ImageService imageService;
    @Autowired
    private ImageRepository imageRepository;


    @GetMapping("/AllImages")
    public String getAllImages(Model model) {
        List<Images> images = imageRepository.findAll();
        model.addAttribute("images", images);
        return "images";
    }


    @GetMapping("/ImageById/{id}")
    public ResponseEntity<?> getImage(@PathVariable Long id) {
        try {
            byte[] imageData = imageService.getImageById
                    (id, "src\\main\\resources\\static\\images");
            if (imageData != null) {
                var resource = new ByteArrayResource(imageData);
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, "image/jpeg");
                return ResponseEntity.ok().headers(headers).body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving image");
        }
    }


    @GetMapping("/ImageList")
    public ResponseEntity<List<String>> getAllImageNames() {
        List<String> imageNames = imageService.getAllImageNames("src/main/resources/static/images");

        if (imageNames != null && !imageNames.isEmpty()) {
            return ResponseEntity.ok(imageNames);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @GetMapping("/File/{fileName}")
    public ResponseEntity<?> getImageByName(@PathVariable String fileName) {
        try {
            byte[] recordData = imageService.getImageByName("src/main/resources/static/images",
                    fileName);

            if (recordData != null) {
                var resource = new ByteArrayResource(recordData);
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, "image/jpeg");
                return ResponseEntity.ok().headers(headers).body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found");
            }

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error: " + e.getMessage());
        }
    }
}
