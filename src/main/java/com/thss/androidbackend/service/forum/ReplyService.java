package com.thss.androidbackend.service.forum;

import com.thss.androidbackend.model.document.Reply;
import com.thss.androidbackend.model.dto.post.ReplyCreateDto;

public interface ReplyService {
    Reply create(ReplyCreateDto dto);
    Reply getReply(String replyId);
    void delete (String replyId);
}
