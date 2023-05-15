package com.thss.androidbackend.repository.EventHandler;

import com.thss.androidbackend.model.document.Post;
import com.thss.androidbackend.repository.UserRepository;
import com.thss.androidbackend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler(Post.class)
public class PostEventHandler {

    @HandleAfterCreate
    public void handlePostCreate(Post post) {
        System.out.println("Post created: " + post.toString());
    }
}