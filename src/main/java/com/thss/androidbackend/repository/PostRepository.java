package com.thss.androidbackend.repository;


import com.thss.androidbackend.model.document.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

public interface PostRepository extends MongoRepository<Post, String> {

    @Query(value = "{ 'postTitle' : :#{#postTitle} }")
    Post findByPostTitleLike(String pattern);
}
