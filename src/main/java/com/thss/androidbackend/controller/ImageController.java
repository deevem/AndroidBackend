package com.thss.androidbackend.controller;

import com.thss.androidbackend.model.document.Post;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/image")
public class ImageController {

    private static String UPLOAD_PATH = "File/image";

    @GetMapping(value = "/{id}")
    public ResponseEntity<Resource> getImage(@PathVariable String id) {
        String contentDisposition = ContentDisposition.builder("attachment").filename(id).build().toString();
        return ResponseEntity.ok().header("Content-Disposition", contentDisposition).body(new FileSystemResource("File/image/" + id));
    }

    @PostMapping(value = "")
    public @ResponseBody ResponseEntity<?> addImage(@NotNull HttpServletRequest httpServletRequest){
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) httpServletRequest;
        Iterator<String> fileNames = multipartHttpServletRequest.getFileNames();
        List<MultipartFile> files = multipartHttpServletRequest.getFiles(fileNames.next());


        MultipartFile image = files.get(0);
        try {
            Random random = new Random();
            String filename = image.getOriginalFilename();
            String name = random.nextLong() + filename.substring(filename.lastIndexOf("."));
            if(!image.getContentType().substring(0, 5).equals("image")){
                return ResponseEntity.badRequest().body("invalid type");
            }
            InputStream inputStream = image.getInputStream();
            Path directory = Paths.get(UPLOAD_PATH);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
            Files.copy(inputStream, directory.resolve(name));
            return ResponseEntity.created(URI.create("/image/" + name)).body("upload success");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("upload failed");
        }
    }
}
