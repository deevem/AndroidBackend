package com.thss.androidbackend.service.user.impl;

import com.thss.androidbackend.model.document.User;
import com.thss.androidbackend.model.dto.register.EmailRegisterDto;
import com.thss.androidbackend.model.dto.register.PhoneRegisterDto;
import com.thss.androidbackend.model.dto.register.UsernameRegisterDto;
import com.thss.androidbackend.repository.UserRepository;
import com.thss.androidbackend.service.user.CounterService;
import com.thss.androidbackend.service.user.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CounterService counterService;

    @Override
    public User create(@NotNull UsernameRegisterDto dto){
        if (userRepo.findByUsername(dto.username()) != null) {
            throw new RuntimeException("Username already exists");
        }
        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        System.out.println(passwordEncoder.encode(dto.password()));
        user.setId(counterService.getNextSequence("customSequences"));
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
