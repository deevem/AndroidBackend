package com.thss.androidbackend.model.vo.forum;

import com.thss.androidbackend.model.document.Reply;
import com.thss.androidbackend.model.document.User;
import com.thss.androidbackend.model.document.projections.Usermeta;
import com.thss.androidbackend.model.vo.user.UserMeta;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record PostCover (
    String id,
    UserMeta creator,
    Long createTime,
    String title,
    String content,
    String videoUrl,
    List<String> images,
    List<String> tags,
    int likesNumber,
    int collectedNumber,
    List<ReplyVo> comments,
    boolean liked,
    boolean collected,
    String location

) implements Serializable {

}
