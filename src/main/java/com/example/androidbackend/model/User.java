package com.example.androidbackend.model;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

public class User {
    @Id private String id;
    private String userName;
    private int password;
    private String userIconUrl = "";
    private final Date createTime = new Date();
    private Boolean banned = false;
    private List<User> followList = List.of();
    private List<User> subscriberList = List.of();
    private List<Post> postList = List.of();
    private List<String> interestedTags = List.of();

    public List<Post> getPostList() {
        return postList;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public List<User> getFollowList() {
        return followList;
    }

    public void setFollowList(List<User> followList) {
        this.followList = followList;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public List<User> getSubscriberList() {
        return subscriberList;
    }

    public String getUserIconUrl() {
        return userIconUrl;
    }

    public void setUserIconUrl(String userIconUrl) {
        this.userIconUrl = userIconUrl;
    }

    public User(String userName, String password){
        this.userName = userName;
        this.password = password.hashCode();
    }
}
