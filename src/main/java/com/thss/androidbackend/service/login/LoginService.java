package com.thss.androidbackend.service.login;

import com.thss.androidbackend.model.dto.login.EmailLoginDto;
import com.thss.androidbackend.model.dto.login.PhoneLoginDto;
import com.thss.androidbackend.model.dto.login.UsernameLoginDto;
import jakarta.validation.constraints.NotNull;

public interface LoginService {
    String login(@NotNull UsernameLoginDto dto);
    String login(@NotNull EmailLoginDto dto);
    String login(@NotNull PhoneLoginDto dto);

    void logout();
}
