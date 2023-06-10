package com.thss.androidbackend.model.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thss.androidbackend.model.vo.forum.PostCover;
import com.thss.androidbackend.model.vo.user.UserMeta;
import com.thss.androidbackend.model.vo.user.UserVo;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

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
    private String description = "There is no description yet.";
    private String avatarUrl = "";
    private Long createTime = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    private Long lastLoginTime = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    private Boolean banned = false;
    @DBRef(lazy = true)
    private List<User> followList = new ArrayList<>(); // person who the user follows
    @DBRef(lazy = true)
    private List<User> subscriberList = new ArrayList<>();
    @DBRef(lazy = true)
    private List<Post> postList = new ArrayList<>();
    private List<String> interestedTags = new ArrayList<>();
    private List<String> roles = new ArrayList<>();
    @DBRef(lazy = true)
    private List<Post> collection = new ArrayList<>();
    @DBRef(lazy = true)
    private List<User> blackList = new ArrayList<>();

    public UserMeta getMeta() {
        return new UserMeta(
                id,
                username,
                avatarUrl
        );
    }

    public String getUserId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof User) {
            return ((User) obj).getId().equals(this.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
