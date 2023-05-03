package com.thss.androidbackend.service.register;

import com.thss.androidbackend.model.dto.register.EmailRegisterDto;
import com.thss.androidbackend.model.dto.register.PhoneRegisterDto;
import com.thss.androidbackend.model.dto.register.UsernameRegisterDto;
import com.thss.androidbackend.model.vo.ResponseVo;
import com.thss.androidbackend.model.vo.register.RegisterResultVo;
import jakarta.validation.constraints.NotNull;

public interface RegisterService {
    RegisterResultVo register(@NotNull UsernameRegisterDto dto);

    RegisterResultVo register(@NotNull EmailRegisterDto dto);

    RegisterResultVo register(@NotNull PhoneRegisterDto dto);


}
