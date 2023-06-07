package com.thss.androidbackend.service.post;

import com.thss.androidbackend.model.document.Post;
import com.thss.androidbackend.model.dto.post.PostCreateDto;
import com.thss.androidbackend.model.vo.forum.PostCover;
import com.thss.androidbackend.model.vo.forum.PostDetail;

public interface PostService {
    void create(PostCreateDto dto);
    PostCover getPostCover(String postId);
    PostCover getPostCover(Post post);
    PostDetail getPostDetail(String postId);
    PostDetail getPostDetail(Post post);
    void like(String postId);
}