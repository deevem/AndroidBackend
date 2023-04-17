package com.example.androidbackend.model;

import org.springframework.data.annotation.Id;

import javax.xml.crypto.Data;
import java.util.List;

public class Post {
    @Id private Long id;
    private User creator;
    private Data createTime;
    private String title;
    private String content;
    private List<Reply> comments;
    private List<>
}
