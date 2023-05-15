package com.thss.androidbackend.service.post.impl;

import com.thss.androidbackend.repository.PostRepository;
import com.thss.androidbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostServiceImpl {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void createPost(){

    }
}
