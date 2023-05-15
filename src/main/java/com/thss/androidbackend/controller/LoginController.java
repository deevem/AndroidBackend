package com.thss.androidbackend.controller;

import com.thss.androidbackend.model.document.User;
import com.thss.androidbackend.model.dto.login.EmailLoginDto;
import com.thss.androidbackend.model.dto.login.PhoneLoginDto;
import com.thss.androidbackend.model.dto.login.UsernameLoginDto;
import com.thss.androidbackend.service.login.LoginService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login")
    public String login(@RequestBody @NotNull UsernameLoginDto dto){
        return loginService.login(dto);
    }
    @PostMapping("/login")
    public String login(@RequestBody @NotNull EmailLoginDto dto){
        return loginService.login(dto);
    }
    @PostMapping("/login")
    public String login(@RequestBody @NotNull PhoneLoginDto dto){
        return loginService.login(dto);
    }
    @GetMapping("/isLogin")
    public String isLogin(){
        return new ;
    }
}
