package com.thss.androidbackend.model.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Document("posts")
@Getter
@Setter
public class Post {
    @Id private String id;
    @DBRef(lazy = true)
    private User creator;
    private Long createTime = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    private Long lastUpdateTime = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    private String title;
    private String content;
    @DBRef(lazy = true)
    private List<Reply> comments = new ArrayList<>();
    private List<String> images = new ArrayList<>();
    @DBRef(lazy = true)
    private List<User> collects = new ArrayList<>();
    @DBRef(lazy = true)
    private List<User> likes = new ArrayList<>();
    private List<String> tag = new ArrayList<>();
    private int shares = 0;
    private String location = "";

}
