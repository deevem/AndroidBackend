package com.thss.androidbackend.model.dto.user;

import java.io.Serializable;

public record UpdatePasswordDto(
        String oldPassword,
        String newPassword
) implements Serializable {
}