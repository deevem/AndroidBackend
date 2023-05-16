package com.thss.androidbackend.controller;

import com.thss.androidbackend.model.document.User;
import com.thss.androidbackend.model.dto.login.EmailLoginDto;
import com.thss.androidbackend.model.dto.login.PhoneLoginDto;
import com.thss.androidbackend.model.dto.login.UsernameLoginDto;
import com.thss.androidbackend.service.login.LoginService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

@RequiredArgsConstructor
@RestController
@RequestMapping("/login")
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/username")
    public ResponseEntity login(@RequestBody @NotNull UsernameLoginDto dto){
        String token = loginService.login(dto);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
    @PostMapping("/email")
    public ResponseEntity login(@RequestBody @NotNull EmailLoginDto dto){
        String token = loginService.login(dto);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
    @PostMapping("/phone")
    public ResponseEntity login(@RequestBody @NotNull PhoneLoginDto dto){
        String token = loginService.login(dto);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
    @GetMapping("/isLogin")
    public String isLogin(){
        System.out.println("isLogin");
        return "success";
    }

    @GetMapping("/logout")
    public ResponseEntity logout(){
        loginService.logout();

        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok().body("logout success");
    }
}
