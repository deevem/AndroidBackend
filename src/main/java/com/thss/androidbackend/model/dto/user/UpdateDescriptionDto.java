package com.thss.androidbackend.model.dto.user;

import java.io.Serializable;

public record UpdateDescriptionDto(
    String description
) implements Serializable {
}