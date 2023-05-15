package com.thss.androidbackend.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ImageController {

    @RequestMapping(method = RequestMethod.GET, value = "/image/{id}")
    public ResponseEntity<Resource> getImage(@PathVariable String id) {
        String contentDisposition = ContentDisposition.builder("attachment").filename(id).build().toString();
        return ResponseEntity.ok().header("Content-Disposition", contentDisposition).body(new FileSystemResource("File/image/" + id));
    }
}
