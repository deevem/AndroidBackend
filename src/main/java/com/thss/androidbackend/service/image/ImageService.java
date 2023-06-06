package com.thss.androidbackend.service.image;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadImage(MultipartFile image);
    String getImage(String imageId);
}
