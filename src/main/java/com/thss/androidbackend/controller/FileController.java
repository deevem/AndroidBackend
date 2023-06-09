package com.thss.androidbackend.controller;

import com.thss.androidbackend.model.vo.ResponseVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.ResponseEntity;
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
import java.util.Random;

@RestController
@RequestMapping("/file")
public class FileController {

    private static String UPLOAD_PATH = "File";

    @GetMapping(value = "/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable String id) {
        System.out.println(id);
        String contentDisposition = ContentDisposition.builder("attachment").filename(id).build().toString();
        System.out.println(id);
        return ResponseEntity.ok().header("Content-Disposition", contentDisposition).body(new FileSystemResource("File/" + id));
    }

    @PostMapping(value = "")
    public @ResponseBody ResponseEntity<?> addImage(@NotNull HttpServletRequest httpServletRequest){
        System.out.println("uploading");
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) httpServletRequest;
        Iterator<String> fileNames = multipartHttpServletRequest.getFileNames();
        List<MultipartFile> files = multipartHttpServletRequest.getFiles(fileNames.next());


        MultipartFile image = files.get(0);
        try {
            Random random = new Random();
            String filename = image.getOriginalFilename();
            String name = random.nextLong() + filename.substring(filename.lastIndexOf("."));
            InputStream inputStream = image.getInputStream();
            Path directory = Paths.get(UPLOAD_PATH);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
            Files.copy(inputStream, directory.resolve(name));
            return ResponseEntity.created(URI.create("/file/" + name)).body("upload success");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("upload failed");
        }
    }

    @PostMapping(value = "/body")
    public @ResponseBody ResponseEntity<?> addImageViaBody(@RequestParam("file") MultipartFile file){

        try {
            Random random = new Random();
            String filename = file.getOriginalFilename();
            String name = random.nextLong() + filename.substring(filename.lastIndexOf("."));
            InputStream inputStream = file.getInputStream();
            Path directory = Paths.get(UPLOAD_PATH);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
            Files.copy(inputStream, directory.resolve(name));
            return ResponseEntity.created(URI.create("/file/" + name)).body(new ResponseVo<>("success"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("upload failed");
        }
    }
}
