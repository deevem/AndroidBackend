package com.thss.androidbackend.model.dto.register;

import java.io.Serializable;

public record PhoneRegisterDto (
    String phoneNumber,
    String code
) implements Serializable {
}