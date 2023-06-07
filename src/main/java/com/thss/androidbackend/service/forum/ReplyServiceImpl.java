package com.thss.androidbackend.service.forum;

import com.thss.androidbackend.model.document.Reply;
import com.thss.androidbackend.model.document.User;
import com.thss.androidbackend.model.dto.post.ReplyCreateDto;
import com.thss.androidbackend.repository.ReplyRepository;
import com.thss.androidbackend.service.security.SecurityService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{
    private final ReplyRepository replyRepository;
    private final SecurityService securityService;
    @Override
    public Reply create(ReplyCreateDto dto) {
        Reply reply = new Reply();
        User creator = securityService.getCurrentUser();
        reply.setCreator(creator);
        reply.setContent(dto.content());
        replyRepository.save(reply);
        return reply;
    }

    @Override
    public Reply getReply(String replyId) {
        return replyRepository.findById(replyId).orElseThrow(
                () -> new RuntimeException("Reply not found")
        );
    }

    @Override
    public void delete(String replyId) {
        Reply reply = getReply(replyId);
        replyRepository.delete(reply);
    }
}
