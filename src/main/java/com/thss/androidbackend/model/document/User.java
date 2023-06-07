package com.thss.androidbackend.model.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document("users")
@Getter
@Setter
public class User {
    @Id
    @Indexed(unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String username;
    @JsonIgnore
    private String password;
    @Indexed(unique = true)
    private String email;
    @Indexed(unique = true)
    private String phoneNumber;
    private String Nickname;
    private String description = "There is no description yet.";
    private String avatarUrl = "";
    private LocalDateTime createTime = LocalDateTime.now();
    private LocalDateTime lastLoginTime = LocalDateTime.now();
    private Boolean banned = false;
    @DBRef(lazy = true)
    private Set<User> followList = new HashSet<>();
    @DBRef(lazy = true)
    private Set<User> subscriberList = new HashSet<>();
    @DBRef(lazy  = true)
    private List<Post> postList = new ArrayList<>();
    private Set<String> interestedTags = new HashSet<>();
    private List<String> roles = new ArrayList<>();

}
