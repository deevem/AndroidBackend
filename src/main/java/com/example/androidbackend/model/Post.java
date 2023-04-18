package com.example.androidbackend.model;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

public class Post {
    @Id private String id;
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
