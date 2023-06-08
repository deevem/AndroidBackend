package com.thss.androidbackend.controller;

import com.thss.androidbackend.model.document.ChatMessage;
import com.thss.androidbackend.model.document.User;
import com.thss.androidbackend.model.vo.chat.ChatHistoryVo;
import com.thss.androidbackend.model.vo.chat.ChatVo;
import com.thss.androidbackend.model.vo.chat.UserChatVo;
import com.thss.androidbackend.model.vo.user.UserMeta;
import com.thss.androidbackend.repository.ChatMessageRepository;
import com.thss.androidbackend.repository.UserRepository;
import com.thss.androidbackend.service.security.SecurityService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final SecurityService securityService;

    @GetMapping(value = "/chat/history/{senderId}/{receiverId}")
    public @ResponseBody ResponseEntity chatHistory(@NotNull @PathVariable String senderId, @NotNull @PathVariable String receiverId) {
        Optional<User> sender = userRepository.findById(senderId);
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
        for (ChatMessage chatMessage : allMessage) {
            if (chatMessage.getSenderId().equals(senderId) && chatMessage.getReceiverId().equals(receiverId)) {
                System.out.println(chatMessage.getMessage());
                res.add(new com.thss.androidbackend.model.vo.chat.ChatMessage(senderUserMeta, receiverUserMeta, chatMessage.getMessage()));
            }
        }

        ChatHistoryVo chatHistoryVo = new ChatHistoryVo(res);
        return ResponseEntity.ok().body(chatHistoryVo);
    }

    @GetMapping(value = "/chat/userchat")
    public @ResponseBody ResponseEntity getUserChat() {
        User currentUser = securityService.getCurrentUser();
        String currentUserId = currentUser.getId();

        List<ChatVo> res = new ArrayList<>();

        List<ChatMessage> allMessage = chatMessageRepository.findAll();
        HashMap<String, ChatVo> hashMap = new HashMap<>();

        UserMeta curruentUserMeta = new UserMeta(currentUserId, currentUser.getUsername(), currentUser.getAvatarUrl());
        for (ChatMessage chatMessage : allMessage) {
            if (chatMessage.getSenderId().equals(currentUserId)) {
                String receiverId = chatMessage.getReceiverId();
                Optional<User> receiver = userRepository.findById(receiverId);
                if (receiver.isEmpty())
                    continue;
                UserMeta receiverUserMeta = new UserMeta(receiverId, receiver.get().getUsername(), receiver.get().getAvatarUrl());

                if (!hashMap.containsKey(receiverId)) {
                    hashMap.put(receiverId, new ChatVo(curruentUserMeta, receiverUserMeta, new ArrayList<>()));
                }
                hashMap.get(receiverId).messages().add(new com.thss.androidbackend.model.vo.chat.ChatMessage(curruentUserMeta, receiverUserMeta, chatMessage.getMessage()));
            } else if (chatMessage.getReceiverId().equals(currentUserId)) {
                String senderId = chatMessage.getSenderId();
                Optional<User> sender = userRepository.findById(senderId);
                if (sender.isEmpty())
                    continue;
                UserMeta receiverUserMeta = new UserMeta(senderId, sender.get().getUsername(), sender.get().getAvatarUrl());

                if (!hashMap.containsKey(senderId)) {
                    hashMap.put(senderId, new ChatVo(curruentUserMeta, receiverUserMeta, new ArrayList<>()));
                }
                hashMap.get(senderId).messages().add(new com.thss.androidbackend.model.vo.chat.ChatMessage(receiverUserMeta, curruentUserMeta, chatMessage.getMessage()));
            }
        }

        for (String key: hashMap.keySet()) {
            res.add(hashMap.get(key));
        }
        System.out.println(res);
        UserChatVo userChatVo = new UserChatVo(res);
        return ResponseEntity.ok().body(userChatVo);
    }
}
