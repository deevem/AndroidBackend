package com.thss.androidbackend.service.post;

import com.thss.androidbackend.model.dto.post.PostCreateDto;
import com.thss.androidbackend.model.vo.post.PostCover;
import com.thss.androidbackend.model.vo.post.PostCoverList;

public interface PostService {
    void create(PostCreateDto dto);
    PostCover getPostCover(String postId);
    void like(String postId);

    PostCoverList getAllPost();
}
