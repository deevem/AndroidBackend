package com.thss.androidbackend.model.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Reply {
    @Id
    private String id;
    @DBRef(lazy = true)
    private User creator;
    @DBRef(lazy = true)
    private List<User> likes = List.of();
    @DBRef(lazy = true)
    private List<Reply> comments = List.of();
    private String content;
    private Long createTime;
}