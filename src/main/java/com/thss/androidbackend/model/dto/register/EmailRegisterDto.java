package com.thss.androidbackend.model.dto.register;

import java.io.Serializable;

public record EmailRegisterDto (
    String email,
    String password

) implements Serializable {
}
