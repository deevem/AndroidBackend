package com.thss.androidbackend.model.vo.user;

import com.thss.androidbackend.model.document.Post;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

public record UserDetail (
        String id,
        String avatar,
        String nickname,
        String description,
        int followNum,
        int subscriberNum,
        Page<Post> postListPage,
        List<String> interestedTags,
        boolean isFollowed



) implements Serializable {

}
