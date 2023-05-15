package com.thss.androidbackend.model.dto.login;

import java.io.Serializable;

public record PhoneLoginDto (
    String phone,
    String code
) implements Serializable {
}
