package com.thss.androidbackend.controller;

import com.thss.androidbackend.exception.CustomException;
import com.thss.androidbackend.model.document.Post;
import com.thss.androidbackend.model.document.User;
import com.thss.androidbackend.model.dto.post.PostCreateDto;
import com.thss.androidbackend.model.vo.TokenVo;
import com.thss.androidbackend.model.vo.post.PostCover;
import com.thss.androidbackend.repository.PostRepository;
import com.thss.androidbackend.repository.UserRepository;
import com.thss.androidbackend.service.post.PostService;
import com.thss.androidbackend.service.security.SecurityService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import java.util.Optional;

@RepositoryRestController
@RequiredArgsConstructor
public class PostController {
    private static String UPLOAD_PATH = "File/image";
    @Resource
    private final PostRepository postRepository;
    private final PostService postService;
    private final UserRepository userRepository;
    private final SecurityService securityService;

    @PostMapping(value = "/posts/create")
    public @ResponseBody ResponseEntity<?> post(@NotNull @RequestBody PostCreateDto dto) {
        try {
            postService.create(dto);
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity(e.getMessage(), e.getStatus());
        }
    }

    @GetMapping(value = "/posts/{id}")
    public @ResponseBody ResponseEntity<?> getPost(@PathVariable String id) {
        Optional<Post> post = postRepository.findById(id);
        if(post.isEmpty()) {
            return new ResponseEntity("Post not found", HttpStatus.NOT_FOUND);
        }
        PostCover cover = postService.getPostCover(id);
        return ResponseEntity.ok().body(cover);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/posts/{id}/uploadImage")

    public @ResponseBody ResponseEntity<?> addImage(@NotNull HttpServletRequest httpServletRequest, @PathVariable String id){
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) httpServletRequest;
        Iterator<String> fileNames = multipartHttpServletRequest.getFileNames();
        List<MultipartFile> files = multipartHttpServletRequest.getFiles(fileNames.next());


        for (MultipartFile image : files) {
            try {
                String name = image.getOriginalFilename();
                System.out.println(name);

                InputStream inputStream = image.getInputStream();
                Path directory = Paths.get(UPLOAD_PATH);
                if (!Files.exists(directory)) {
                    Files.createDirectories(directory);
                }
                long copy = Files.copy(inputStream, directory.resolve(name));
                Optional<Post> post = postRepository.findById(id);
                if (post.isEmpty()){
                    return ResponseEntity.badRequest().body("post not found");
                } else {
                    post.get().getImages().add(name);
                    postRepository.save(post.get());
                    return ResponseEntity.ok("upload success");
                }

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body("upload failed");
            }
        }
        return ResponseEntity.badRequest().body("upload failed");
    }
    @PostMapping(value = "/posts/{id}/like")
    public @ResponseBody ResponseEntity like(@NotNull @PathVariable String id) {
        try {
            postService.like(id);
            return ResponseEntity.ok("like success");
        } catch (CustomException e) {
            return new ResponseEntity(e.getMessage(), e.getStatus());
        }
    }
}
