package com.thss.androidbackend.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.thss.androidbackend.model.document.ChatMessage;
import com.thss.androidbackend.model.document.User;
import com.thss.androidbackend.repository.ChatMessageRepository;
import com.thss.androidbackend.repository.UserRepository;
import com.thss.androidbackend.service.security.JwtUtils;
import com.thss.androidbackend.service.security.SecurityService;
import com.thss.androidbackend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class ChatWebsocketHandler extends TextWebSocketHandler {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    class MessageToClient {
        private String senderId;
        private String message;

        public MessageToClient(String senderId, String message) {
            this.senderId = senderId;
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    private static final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

//    private final RedisTemplate<String, String> websocketSessionMapping;
    private final HashMap<String, String> websocketSessionMapping = new HashMap<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println(message.getPayload());

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = message.getPayload();
            JsonNode jsonNode = objectMapper.readTree(json);
            System.out.println(jsonNode);
            String operation = jsonNode.get("operation").asText();
            if (operation.equals("register")) {
                String token = jsonNode.get("token").asText().substring(7);
                String userId = JwtUtils.parse(token).getSubject();
                User currentUser = userRepository.findById(userId).get();
                websocketSessionMapping.put(currentUser.getId(), session.getId());
            } else if (operation.equals("send")) {
                String senderId = jsonNode.get("senderId").asText();
                String receiverId = jsonNode.get("receiverId").asText();
                String messageContent = jsonNode.get("message").asText();

                if (Boolean.FALSE.equals(websocketSessionMapping.containsKey(senderId))) {
                    websocketSessionMapping.put(senderId, session.getId());
                }

                // save message to database
                chatMessageRepository.insert(new ChatMessage(senderId, receiverId, messageContent));

                if (Boolean.TRUE.equals(websocketSessionMapping.containsKey(receiverId))) {
                    // send message to receiver if websocket connect on
                    String receiverSessionId = websocketSessionMapping.get(receiverId);
                    for (WebSocketSession sess : sessions) {
                        if (sess.getId().equals(receiverSessionId)) {
                            // create json object
                            MessageToClient messageToClient = new MessageToClient(senderId, messageContent);
                            ObjectMapper mapper = new ObjectMapper();
                            String jsonString = mapper.writerWithDefaultPrettyPrinter()
                                    .writeValueAsString(messageToClient);
                            sess.sendMessage(new TextMessage(jsonString));
                        }
                    }
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }
}
