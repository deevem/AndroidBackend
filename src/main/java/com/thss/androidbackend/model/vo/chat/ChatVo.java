package com.thss.androidbackend.model.vo.chat;

import com.thss.androidbackend.model.vo.user.UserMeta;

import java.io.Serializable;
import java.util.List;

public record ChatVo(
        UserMeta selfUserMeta,
        UserMeta targetUserMeta,
        List<ChatMessage> messages
) implements Serializable {

}
