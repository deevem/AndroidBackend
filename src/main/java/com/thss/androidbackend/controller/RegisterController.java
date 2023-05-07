package com.thss.androidbackend.controller;

import com.thss.androidbackend.model.dto.register.EmailRegisterDto;
import com.thss.androidbackend.model.dto.register.PhoneRegisterDto;
import com.thss.androidbackend.model.dto.register.UsernameRegisterDto;
import com.thss.androidbackend.model.vo.ResponseVo;
import com.thss.androidbackend.model.vo.register.RegisterResultVo;
import com.thss.androidbackend.service.register.RegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {

    private final RegisterService registerService;

    @PostMapping("/username")
    public ResponseEntity<ResponseVo<RegisterResultVo>> register(@Valid @RequestBody UsernameRegisterDto dto){
        return ResponseEntity.ok(new ResponseVo<>(registerService.register(dto)));
    }
    @PostMapping("/email")
    public ResponseEntity<ResponseVo<RegisterResultVo>> register(@Valid @RequestBody EmailRegisterDto dto){
        return ResponseEntity.ok(new ResponseVo<>(registerService.register(dto)));
    }
    @PostMapping("/phone")
    public ResponseEntity<ResponseVo<RegisterResultVo>> register(@Valid @RequestBody PhoneRegisterDto dto){
        return ResponseEntity.ok(new ResponseVo<>(registerService.register(dto)));
    }
}