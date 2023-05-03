package com.thss.androidbackend.repository;


import com.thss.androidbackend.model.document.Post;
import org.springframework.data.mongodb.repository.Query;

public interface PostRepository {
    Post findByPostId(String postId);

    @Query(value = "{ 'postTitle' : :#{#postTitle} }")
    Post findByPostTitleLike(String pattern);
}
