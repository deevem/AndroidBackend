package com.thss.androidbackend.repository;

import com.thss.androidbackend.model.document.Reply;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReplyRepository extends MongoRepository<Reply, String> {
}
