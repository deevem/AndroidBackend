package com.thss.androidbackend.service.image;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

@Service
public class ImageServiceImpl implements ImageService {
    private static String UPLOAD_PATH = "File/image";

    public String uploadImage(MultipartFile image){
        try{
            Random random = new Random();
            String filename = image.getOriginalFilename();
            String name = random.nextLong() + filename.substring(filename.lastIndexOf("."));
            if(!image.getContentType().substring(0, 5).equals("image")){
                return null;
            }
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
