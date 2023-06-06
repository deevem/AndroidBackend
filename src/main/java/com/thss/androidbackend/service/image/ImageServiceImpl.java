package com.thss.androidbackend.service.image;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageServiceImpl implements ImageService {
    private static String UPLOAD_PATH = "File/image";

    public String uploadImage(MultipartFile image){
        try{
            String name = image.getOriginalFilename();
            InputStream inputStream = image.getInputStream();
            Path directory = Paths.get(UPLOAD_PATH);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
            Files.copy(inputStream, directory.resolve(name));
            return name;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }

    }
    public String getImage(String imageId) {
        return null;
    }
}
