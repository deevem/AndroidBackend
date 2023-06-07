package com.thss.androidbackend.controller;

import com.thss.androidbackend.model.document.ChatMessage;
import com.thss.androidbackend.model.document.User;
import com.thss.androidbackend.model.vo.chat.ChatHistoryVo;
import com.thss.androidbackend.model.vo.user.UserMeta;
import com.thss.androidbackend.repository.ChatMessageRepository;
import com.thss.androidbackend.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    @GetMapping(value = "/chat/history/{senderId}/{receiverId}")
    public @ResponseBody ResponseEntity chatHistory(@NotNull @PathVariable String senderId, @NotNull @PathVariable String receiverId) {
        Optional<User> sender =  userRepository.findById(senderId);
        Optional<User> receiver = userRepository.findById(receiverId);

        if (sender.isEmpty() || receiver.isEmpty()) {
            return ResponseEntity.ok().body(new ChatHistoryVo(new ArrayList<>()));
        }

        UserMeta senderUserMeta = new UserMeta(senderId, sender.get().getAvatarUrl(), sender.get().getUsername());
        UserMeta receiverUserMeta = new UserMeta(receiverId, receiver.get().getAvatarUrl(), receiver.get().getUsername());

        System.out.println(senderId);
        System.out.println(receiverId);
        List<ChatMessage> allMessage = chatMessageRepository.findAll();
        List<com.thss.androidbackend.model.vo.chat.ChatMessage> res = new ArrayList<>();
        for (ChatMessage chatMessage: allMessage) {
            if (chatMessage.getSenderId().equals(senderId) && chatMessage.getReceiverId().equals(receiverId)) {
                System.out.println(chatMessage.getMessage());
                res.add(new com.thss.androidbackend.model.vo.chat.ChatMessage(senderUserMeta, receiverUserMeta, chatMessage.getMessage()));
            }
        }

        ChatHistoryVo chatHistoryVo = new ChatHistoryVo(res);
        return ResponseEntity.ok().body(chatHistoryVo);
    }
}
