package com.thss.androidbackend.model.vo;

import com.thss.androidbackend.model.vo.user.UserMeta;

import java.io.Serializable;
import java.util.List;

public record NotificationVo(
    String id,
    UserMeta targetUserMeta,
    UserMeta sourceUserMeta,
    String route,
    String title,
    String content,
    Boolean readFlag
) implements Serializable {
}

