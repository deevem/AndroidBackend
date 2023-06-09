package com.thss.androidbackend.service.notification;

import com.thss.androidbackend.model.document.Post;
import com.thss.androidbackend.model.document.User;

public interface NotificationService {

    void createLikeNotification(User targetUser, User sourceUser, Post likedPost);

    void createReplyNotification(User targetUser, User sourceUser, Post likedPost);

    void createNewPostNotification(User targetUser, User sourceUser, Post post);
}
