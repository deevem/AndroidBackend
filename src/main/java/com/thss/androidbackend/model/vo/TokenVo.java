package com.thss.androidbackend.model.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

public record TokenVo (
    String token
) implements Serializable {

}
