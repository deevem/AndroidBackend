package com.thss.androidbackend.service.forum;

import com.thss.androidbackend.model.document.Post;
import com.thss.androidbackend.model.document.Reply;
import com.thss.androidbackend.model.vo.forum.PostCover;
import com.thss.androidbackend.model.vo.forum.PostDetail;

import java.util.List;

public interface PostService {
    void create(String title, String content, List<String> images, List<String> tag, String location, String videoUrl);
    PostCover getPostCover(String postId);
    PostCover getPostCover(Post post);
    PostDetail getPostDetail(String postId);
    PostDetail getPostDetail(Post post);
    void like(String postId);
    void unLike(String postId);
    void collect(String postId);
    void unCollect(String postId);
    void addReply(String postId, Reply reply);
    void deleteReply(String postId, String replyId);
    List<Post> search(String keyword);
}
