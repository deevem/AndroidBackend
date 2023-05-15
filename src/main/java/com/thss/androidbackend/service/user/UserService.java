package com.thss.androidbackend.service.user;

import com.thss.androidbackend.model.document.User;
import com.thss.androidbackend.model.dto.register.EmailRegisterDto;
import com.thss.androidbackend.model.dto.register.PhoneRegisterDto;
import com.thss.androidbackend.model.dto.register.UsernameRegisterDto;
import jakarta.validation.constraints.NotNull;

public interface UserService {
    public User create(@NotNull UsernameRegisterDto dto);
    public User create(@NotNull EmailRegisterDto dto);
    public User create(@NotNull PhoneRegisterDto dto);

    public String generateToken(@NotNull User user);
}
