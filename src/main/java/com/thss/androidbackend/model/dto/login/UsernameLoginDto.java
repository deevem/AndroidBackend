package com.thss.androidbackend.model.dto.login;

import java.io.Serializable;

public record UsernameLoginDto
        (
                String username,
                String password
        )
implements Serializable {
}
