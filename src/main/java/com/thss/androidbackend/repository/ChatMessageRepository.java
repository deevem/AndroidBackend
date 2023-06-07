package com.thss.androidbackend.repository;

import com.thss.androidbackend.model.document.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

}
