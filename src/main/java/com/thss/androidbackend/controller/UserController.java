package com.thss.androidbackend.controller;


import com.thss.androidbackend.model.dto.user.UpdateDescriptionDto;
import com.thss.androidbackend.model.dto.user.UpdateNicknameDto;
import com.thss.androidbackend.model.dto.user.UpdatePasswordDto;
import com.thss.androidbackend.repository.UserRepository;
import com.thss.androidbackend.service.image.ImageService;
import com.thss.androidbackend.service.user.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RepositoryRestController
@RequiredArgsConstructor
public class UserController {
    @Resource
    private final UserRepository userRepository;
    private final UserService userService;
    private final ImageService imageService;

    @PostMapping("/users/{id}/subscribe")
    ResponseEntity<?> subscribe(@PathVariable String id){
        userService.subscribe(id);
        return ResponseEntity.ok().body("like success");
    }
    @PostMapping("/users/{id}/unsubscribe")
    ResponseEntity<?> unsubscribe(@PathVariable String id){
        userService.unsubscribe(id);
        return ResponseEntity.ok().body("unlike success");
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
    @PostMapping("/users/update/avatar")
    ResponseEntity<?> updateAvatar(@NotNull HttpServletRequest httpServletRequest){
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) httpServletRequest;
        MultipartFile avatar = multipartHttpServletRequest.getFile("avatar");
        imageService.uploadImage(avatar);
        userService.updateAvatar(avatar.getOriginalFilename());
        return ResponseEntity.ok().body("update avatar success");
    }
    @PostMapping("/users/update/password")
    ResponseEntity<?> updatePassword(@RequestBody @NotNull UpdatePasswordDto dto){
        userService.updatePassword(dto);
        return ResponseEntity.ok().body("update password success");
    }
}