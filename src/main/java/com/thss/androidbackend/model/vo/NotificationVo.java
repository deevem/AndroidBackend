package com.thss.androidbackend.model.vo;

import com.thss.androidbackend.model.vo.user.UserMeta;

import java.io.Serializable;

public record NotificationVo(
    UserMeta targetUserMeta,
    UserMeta sourceUserMeta,
    String route,
    String title,
    String content
) implements Serializable {
}
