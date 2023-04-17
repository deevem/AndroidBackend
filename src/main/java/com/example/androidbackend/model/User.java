package com.example.androidbackend.model;

import org.springframework.data.annotation.Id;

import java.util.List;

public class User {
    @Id private Long id;
    private String userName;
    private String userIconUrl;
    private List<User> followList;
    private List<User> subscriberList;
    private List<Blog>
}