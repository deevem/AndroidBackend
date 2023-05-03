package com.thss.androidbackend.model.document;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Reply {
    private User creator;
    private List<User> likes = List.of();
    private List<Reply> comments = List.of();
    private String content;


}