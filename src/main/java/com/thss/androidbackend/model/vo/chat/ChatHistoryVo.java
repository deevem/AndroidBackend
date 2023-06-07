package com.thss.androidbackend.model.vo.chat;

import java.io.Serializable;
import java.util.List;

public record ChatHistoryVo(
    List<ChatMessage> chatHistory
) implements Serializable {
}
