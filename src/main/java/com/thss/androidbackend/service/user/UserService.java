package com.thss.androidbackend.service.user;

import com.thss.androidbackend.model.document.NotificationMessage;
import com.thss.androidbackend.model.document.User;
import com.thss.androidbackend.model.dto.register.EmailRegisterDto;
import com.thss.androidbackend.model.dto.register.PhoneRegisterDto;
import com.thss.androidbackend.model.dto.register.UsernameRegisterDto;
import com.thss.androidbackend.model.dto.user.UpdatePasswordDto;
import com.thss.androidbackend.model.vo.user.UserDetail;
import com.thss.androidbackend.model.vo.user.UserMeta;
import com.thss.androidbackend.model.vo.user.UserVo;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface UserService {
    User create(@NotNull UsernameRegisterDto dto);
    User create(@NotNull EmailRegisterDto dto);
    User create(@NotNull PhoneRegisterDto dto);
    void subscribe(@NotNull String userId);
    void unsubscribe(@NotNull String userId);
    void black(@NotNull String userId);
    void unblack(@NotNull String userId);
    void updateDescription(@NotNull String description);
    void updateAvatar(@NotNull String avatar);
    void ban(@NotNull String userId);
    void updateNickname(String nickname);
    void updateUsername(String username);
    void updatePassword(UpdatePasswordDto dto);
    UserMeta getUserMeta(@NotNull String userId);
    UserMeta getUserMeta(@NotNull User user);
    UserDetail getUserDetail(@NotNull String userId);
    UserDetail getUserDetail(@NotNull User user);
    UserVo getUserVoByUser(@NotNull User user);
    List<NotificationMessage> getNotificationList(String userId);
}
