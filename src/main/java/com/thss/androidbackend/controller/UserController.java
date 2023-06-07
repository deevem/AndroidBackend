package com.thss.androidbackend.controller;


import com.thss.androidbackend.model.document.Post;
import com.thss.androidbackend.model.document.User;
import com.thss.androidbackend.model.dto.user.UpdateDescriptionDto;
import com.thss.androidbackend.model.dto.user.UpdateNicknameDto;
import com.thss.androidbackend.model.dto.user.UpdatePasswordDto;
import com.thss.androidbackend.model.dto.user.UpdateUsernameDto;
import com.thss.androidbackend.model.vo.forum.PostCover;
import com.thss.androidbackend.model.vo.user.UserMeta;
import com.thss.androidbackend.repository.UserRepository;
import com.thss.androidbackend.service.image.ImageService;
import com.thss.androidbackend.service.post.PostService;
import com.thss.androidbackend.service.security.SecurityService;
import com.thss.androidbackend.service.user.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RepositoryRestController
@RequiredArgsConstructor
public class UserController {
    @Resource
    private final UserRepository userRepository;
    private final UserService userService;
    private final ImageService imageService;
    private final PostService postService;
    private final SecurityService securityService;

    @GetMapping("/user")
    ResponseEntity getUser() {
        System.out.println("get current user");
        User currentUser = securityService.getCurrentUser();
        return ResponseEntity.ok().body(currentUser);
    }

    @PostMapping("/users/{id}/subscribe")
    ResponseEntity<?> subscribe(@PathVariable String id){
        userService.subscribe(id);
        return ResponseEntity.ok().body("subscribe success");
    }
    @PostMapping("/users/{id}/unsubscribe")
    ResponseEntity<?> unsubscribe(@PathVariable String id){
        userService.unsubscribe(id);
        return ResponseEntity.ok().body("unsubscribe success");
    }
    @PostMapping("/users/update/description")
    ResponseEntity<?> updateDescription(@RequestBody @NotNull UpdateDescriptionDto dto){
        userService.updateDescription(dto.description());
        return ResponseEntity.ok().body("update description success");
    }
    @PostMapping("/users/update/nickname")
    ResponseEntity<?> updateNickname(@RequestBody @NotNull UpdateNicknameDto dto){
        userService.updateNickname(dto.nickname());
        return ResponseEntity.ok().body("update nickname success");
    }
    @PostMapping("/users/update/username")
    ResponseEntity<?> updateUsername(@RequestBody @NotNull UpdateUsernameDto dto){
        userService.updateUsername(dto.username());
        return ResponseEntity.ok().body("update username success");
    }
    @PostMapping("/users/update/avatar")
    ResponseEntity<?> updateAvatar(@NotNull HttpServletRequest httpServletRequest){
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) httpServletRequest;
        MultipartFile avatar = multipartHttpServletRequest.getFile("avatar");
        String name = imageService.uploadImage(avatar);
        if(name == null) return ResponseEntity.badRequest().body("upload avatar failed");
        userService.updateAvatar(name);
        return ResponseEntity.ok().body("update avatar success");
    }
    @PostMapping("/users/update/password")
    ResponseEntity<?> updatePassword(@RequestBody @NotNull UpdatePasswordDto dto){
        userService.updatePassword(dto);
        return ResponseEntity.ok().body("update password success");
    }
    @GetMapping("/users/{id}/postList")
    ResponseEntity<?> getPostList(@PathVariable String id,
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "10") int size){

        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) return ResponseEntity.badRequest().body("user not found");
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        List<PostCover> postList = user.get().getPostList().stream()
                .map(postService::getPostCover).toList().subList(page * size, Math.min((page + 1) * size, userRepository.findById(id).get().getPostList().size()));
        Page<PostCover> postListPage = new PageImpl<>(postList, pageable, postList.size());
        return ResponseEntity.ok().body(postListPage);
    }
    @GetMapping("/users/{id}/subscriberList")
    ResponseEntity<?> getSubscriberList(@PathVariable String id,
                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "size", defaultValue = "10") int size){
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) return ResponseEntity.badRequest().body("user not found");
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        List<UserMeta> subscribeList = user.get().getSubscriberList().stream()
                .map(uu -> userService.getUserMeta(uu)).toList()
                .subList(page * size, Math.min((page + 1) * size, user.get().getSubscriberList().size()));
        Page<UserMeta> subscribeListPage = new PageImpl<>(subscribeList, pageable, subscribeList.size());
        return ResponseEntity.ok().body(subscribeListPage);
    }
    @GetMapping("/users/{id}/followerList")
    ResponseEntity<?> getFollowerList(@PathVariable String id,
                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "size", defaultValue = "10") int size){
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) return ResponseEntity.badRequest().body("user not found");
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        List<UserMeta> subscribeList = user.get().getFollowList().stream()
                .map(uu -> userService.getUserMeta(uu)).toList()
                .subList(page * size, Math.min((page + 1) * size, user.get().getFollowList().size()));
        Page<UserMeta> subscribeListPage = new PageImpl<>(subscribeList, pageable, subscribeList.size());
        return ResponseEntity.ok().body(subscribeListPage);
    }
}