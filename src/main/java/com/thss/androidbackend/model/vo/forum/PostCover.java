package com.thss.androidbackend.model.vo.forum;

import com.thss.androidbackend.model.vo.user.UserMeta;

import java.io.Serializable;
import java.util.List;

public record PostCover (
    UserMeta creator,
    String title,
    String content,
    List<String> images,
    List<String> tags,
    int likes,
    int comments,
    int shares,
    boolean liked

) implements Serializable {

}