package com.thss.androidbackend.model.vo.chat;

import com.thss.androidbackend.model.vo.user.UserMeta;

import java.io.Serializable;

public record ChatMessage (
        UserMeta senderUserMeta,
        UserMeta receiverUserMeta,
        String message
) implements Serializable {

}
