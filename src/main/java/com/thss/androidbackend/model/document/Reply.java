package com.thss.androidbackend.model.document;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Reply {
    private User creator;
    private String content;
    private Long createTime;
    private List<User> likes;
    private List<Reply> comments;
}