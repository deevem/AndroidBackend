package com.thss.androidbackend.model.dto.register;

import java.io.Serializable;

public record UsernameRegisterDto (
        String username,
        String password
) implements Serializable {
}
