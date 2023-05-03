package com.thss.androidbackend.service.register.impl;

import com.thss.androidbackend.model.document.User;
import com.thss.androidbackend.model.dto.register.EmailRegisterDto;
import com.thss.androidbackend.model.dto.register.PhoneRegisterDto;
import com.thss.androidbackend.model.dto.register.UsernameRegisterDto;
import com.thss.androidbackend.model.vo.register.RegisterResultVo;
import com.thss.androidbackend.service.register.RegisterService;
import com.thss.androidbackend.service.user.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final UserService userService;

    @Override
    public RegisterResultVo register(@NotNull UsernameRegisterDto dto) {
        try {
            User user = userService.create(dto);
            RegisterResultVo result = new RegisterResultVo();
            result.setNickname(user.getNickname());
            result.setUserID(user.getId());
            result.setUsername(user.getUsername());
            return result;
        } catch (Exception e) {

        }
        return null;
    }
    @Override
    public RegisterResultVo register(@NotNull EmailRegisterDto dto) {
        User user = userService.create(dto);
        RegisterResultVo result = new RegisterResultVo();
        result.setNickname(user.getNickname());
        result.setUserID(user.getId());
        result.setUsername(user.getUsername());
        return result;
    }

    @Override
    public RegisterResultVo register(@NotNull PhoneRegisterDto dto) {
        User user = userService.create(dto);
        RegisterResultVo result = new RegisterResultVo();
        result.setNickname(user.getNickname());
        result.setUserID(user.getId());
        result.setUsername(user.getUsername());
        return result;
    }

}
