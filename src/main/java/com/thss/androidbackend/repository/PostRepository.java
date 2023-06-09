package com.thss.androidbackend.repository;


import com.thss.androidbackend.model.document.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    @Query
    List<Post> findByTitleContainingIgnoreCaseAndContentContainingIgnoreCaseAndTagContainsIgnoreCase(String title, String content, String tag);
}
