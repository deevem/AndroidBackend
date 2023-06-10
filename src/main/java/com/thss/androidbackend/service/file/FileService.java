package com.thss.androidbackend.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadImage(MultipartFile image);
    String getImage(String imageId);
}
