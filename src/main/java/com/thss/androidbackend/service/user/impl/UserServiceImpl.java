package com.thss.androidbackend.service.user.impl;

import com.thss.androidbackend.model.document.User;
import com.thss.androidbackend.model.dto.register.EmailRegisterDto;
import com.thss.androidbackend.model.dto.register.PhoneRegisterDto;
import com.thss.androidbackend.model.dto.register.UsernameRegisterDto;
import com.thss.androidbackend.repository.UserRepository;
import com.thss.androidbackend.service.user.UserService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public User create(@NotNull UsernameRegisterDto dto){
        if (userRepo.findByUserName(dto.username()) != null) {
            throw new RuntimeException("Username already exists");
        }
        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(dto.password());
        user.setNickname("thss" + user.getId().toString());
        return userRepo.insert(user);
    }
    @Override
    public User create(@NotNull EmailRegisterDto dto){
        if (userRepo.findByEmail(dto.email()) != null) {
            throw new RuntimeException("Username already exists");
        }
        User user = new User();
        user.setUsername(dto.email());
        user.setPassword(dto.password());
        user.setNickname("thss" + user.getId().toString());
        return userRepo.insert(user);
    }
    @Override
    public User create(@NotNull PhoneRegisterDto dto){
//        if (userRepo.findByPhone(dto.phoneNumber()) != null) {
//            throw new RuntimeException("Username already exists");
//        }
//        User user = new User();
//        user.setUsername(dto.username());
//        user.setNickname("thss" + user.getId().toString());
//        return userRepo.insert(user);
        return null;
    }
}
