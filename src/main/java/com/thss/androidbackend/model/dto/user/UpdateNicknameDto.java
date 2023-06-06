package com.thss.androidbackend.model.dto.user;

import java.io.Serializable;

public record UpdateNicknameDto(
        String nickname
) implements Serializable {
}