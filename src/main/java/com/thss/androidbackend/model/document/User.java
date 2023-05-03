package com.thss.androidbackend.model.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Document("users")
@Getter
@Setter
public class User {
    @Id private String id;
    @Indexed(unique = true)
    private String username;
    private String password;
    @Indexed(unique = true)
    private String email;
    @Indexed(unique = true)
    private String phoneNumber;
    private String Nickname;
    private String description = "There is no description yet.";
    private String userIconUrl = "";
    private LocalDateTime createTime = LocalDateTime.now();
    private LocalDateTime lastLoginTime = LocalDateTime.now();
    private Boolean banned = false;
    private Set<User> followList = new HashSet<>();
    private Set<User> subscriberList = new HashSet<>();
    private Set<Post> postList = new HashSet<>();
    private Set<String> interestedTags = new HashSet<>();

}
