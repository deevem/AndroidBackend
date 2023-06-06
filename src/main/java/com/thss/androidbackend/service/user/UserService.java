package com.thss.androidbackend.service.user;

import com.thss.androidbackend.model.document.User;
import com.thss.androidbackend.model.dto.register.EmailRegisterDto;
import com.thss.androidbackend.model.dto.register.PhoneRegisterDto;
import com.thss.androidbackend.model.dto.register.UsernameRegisterDto;
import com.thss.androidbackend.model.dto.user.UpdatePasswordDto;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface UserService {
    User create(@NotNull UsernameRegisterDto dto);
    User create(@NotNull EmailRegisterDto dto);
    User create(@NotNull PhoneRegisterDto dto);
    void subscribe(@NotNull String userId);
    void unsubscribe(@NotNull String userId);
    void updateDescription(@NotNull String description);
    void updateAvatar(@NotNull String avatar);
    void ban(@NotNull String userId);
    void updateNickname(String nickname);
    void updatePassword(UpdatePasswordDto dto);



}
