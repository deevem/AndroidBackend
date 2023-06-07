package com.thss.androidbackend.model.dto.post;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record ReplyCreateDto(
        String content
) implements Serializable {
}
