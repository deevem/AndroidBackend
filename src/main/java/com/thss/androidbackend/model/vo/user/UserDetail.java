package com.thss.androidbackend.model.vo.user;

import com.thss.androidbackend.model.document.Post;

import java.io.Serializable;
import java.util.List;

public record UserDetail (
        String id,
        String avatar,
        String nickname,
        String description,
        int followNum,
        int subscriberNum,
        List<Post> postList,
        List<String> interestedTags



) implements Serializable {

}
