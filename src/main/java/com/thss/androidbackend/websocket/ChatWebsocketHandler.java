package com.thss.androidbackend.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiredArgsConstructor
public class ChatWebsocketHandler extends TextWebSocketHandler {
    private static final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

//    private final RedisTemplate<String, String> websocketSessionMapping;
    private final HashMap<String, String> websocketSessionMapping = new HashMap<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("test");
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

            String senderId = jsonNode.get("senderId").asText();
            String receiverId = jsonNode.get("receiverId").asText();
            String messageContent = jsonNode.get("message").asText();

            if (Boolean.FALSE.equals(websocketSessionMapping.containsKey(senderId))) {
                websocketSessionMapping.put(senderId, session.getId());
            }

            // save message to database

            if (Boolean.TRUE.equals(websocketSessionMapping.containsKey(receiverId))) {
                // send message to receiver if websocket connect on
                String receiverSessionId = websocketSessionMapping.get(receiverId);
                for (WebSocketSession sess: sessions) {
                    if (sess.getId().equals(receiverSessionId)) {
                        sess.sendMessage(new TextMessage(messageContent));
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
