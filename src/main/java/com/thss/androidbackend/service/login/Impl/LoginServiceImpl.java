package com.thss.androidbackend.service.login.Impl;

import com.thss.androidbackend.model.dto.login.EmailLoginDto;
import com.thss.androidbackend.model.dto.login.PhoneLoginDto;
import com.thss.androidbackend.model.dto.login.UsernameLoginDto;
import com.thss.androidbackend.service.login.LoginService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final AuthenticationManager authenticationManager;
    public String login(@NotNull UsernameLoginDto dto){
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.username(),dto.password()));
            if (authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return "success";
            }
            else return "fail";
        } catch (Exception e) {
            e.printStackTrace();
            return "exception";
        }
    }
    public String login(@NotNull EmailLoginDto dto){
        return null;
    }
    public String login(@NotNull PhoneLoginDto dto){
        return null;
    }
    public void logout() {
        authenticationManager.
    }
}
