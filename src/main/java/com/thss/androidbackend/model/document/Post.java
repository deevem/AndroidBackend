package com.thss.androidbackend.model.document;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document
public class Post {
    private User creator;
    private Date createTime;
    private Date lastUpdateTime;
    private String title;
    private String content;
    private List<Reply> comments;
    private List<String> images;
    private List<User> favorites;
    private List<User> likes;
    private String tag;
}
