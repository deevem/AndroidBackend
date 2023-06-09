package com.thss.androidbackend.repository;

import com.thss.androidbackend.model.document.NotificationMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<NotificationMessage, String> {
}
