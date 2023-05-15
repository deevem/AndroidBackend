package com.thss.androidbackend.model.dto.login;

import java.io.Serializable;

public record EmailLoginDto (
    String email,
    String password
) implements Serializable {
}
