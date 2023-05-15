package com.thss.androidbackend.controller;

import com.thss.androidbackend.model.document.Post;
import com.thss.androidbackend.model.document.User;
import com.thss.androidbackend.model.dto.post.PostCreateDto;
import com.thss.androidbackend.repository.PostRepository;
import com.thss.androidbackend.repository.UserRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.InputStream;
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

    private final UserRepository userRepository;

    @RequestMapping(method = RequestMethod.POST, value = "/posts")
    public @ResponseBody ResponseEntity<?> post(@NotNull @RequestBody PostCreateDto dto) {
        Post newPost = new Post();
        if(dto.authorId() == null) {
            return new ResponseEntity<String>("Author ID cannot be null", HttpStatus.BAD_REQUEST);
        }
        Optional<User> creator = userRepository.findById(dto.authorId());
        if(creator.isEmpty()) {
            return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
        }
        newPost.setCreator(creator.get());
        newPost.setContent(dto.content());
        newPost.setTitle(dto.title());
        postRepository.save(newPost);
        return new ResponseEntity<String>(HttpStatus.CREATED);
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
}
