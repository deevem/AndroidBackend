package com.thss.androidbackend.model.vo.forum;

import com.thss.androidbackend.model.vo.user.UserMeta;
import com.thss.androidbackend.model.vo.user.UserVo;

import java.io.Serializable;
import java.util.List;

public record ReplyVo(
        String id,
        UserMeta creator,
        String content,
        Long createTime,
        List<UserMeta> likes
) implements Serializable {
}
