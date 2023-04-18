package com.example.androidbackend.model;

import org.springframework.data.annotation.Id;

import java.util.List;

public class Reply {
    @Id private String id;
    private User creator;
    private List<User> likes;
    private List<Reply> comments;
    private String content;

}