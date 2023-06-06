package com.thss.androidbackend.model.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;

@Document("posts")
@Getter
@Setter
public class Post {
    @Id private String id;
    @DBRef(lazy = true)
    private User creator;
    private LocalDateTime createTime = LocalDateTime.now();
    private LocalDateTime lastUpdateTime = LocalDateTime.now();
    private String title;
    private String content;
    @DBRef(lazy = true)
    private List<Reply> comments = new ArrayList<>();
    private List<String> images = new ArrayList<>();
    @DBRef(lazy = true)
    private Set<User> favorites = new HashSet<>();
    @DBRef(lazy = true)
    private Set<User> likes = new HashSet<>();
    private Set<String> tag = new HashSet<>();
    private int shares = 0;


}
