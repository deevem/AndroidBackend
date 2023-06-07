package com.thss.androidbackend.model.vo.chat;

import java.io.Serializable;
import java.util.List;

public record UserChatVo(List<List<ChatMessage>> userChat) implements Serializable { }
