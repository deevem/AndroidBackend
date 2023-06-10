package com.thss.androidbackend.model.vo.user;

import com.thss.androidbackend.model.vo.forum.PostCover;

import java.io.Serializable;
import java.util.List;

public record UserVo(
        String id,
        String username,
        String email,
        String phoneNumber,
        String description,
        String avatarUrl,
        Long createTime,
        Long lastLoginTime,
        String banned,
        List<UserMeta> followList,
        List<UserMeta> subscriberList,
        List<PostCover> postList,
        List<String> interestedTags,
        List<String> roles,
        List<PostCover> collection,
        List<UserMeta> blackList

) implements Serializable {

}
