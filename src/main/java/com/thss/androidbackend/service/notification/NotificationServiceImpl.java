package com.thss.androidbackend.service.notification;

import com.thss.androidbackend.model.document.NotificationMessage;
import com.thss.androidbackend.model.document.Post;
import com.thss.androidbackend.model.document.User;
import com.thss.androidbackend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private void createNotification(User targetUser, User sourceUser, String title, String content, String route) {
        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setUserToNotify(targetUser);
        notificationMessage.setCreator(sourceUser);
        notificationMessage.setTitle(title);
        notificationMessage.setContent(content);
        notificationMessage.setRoute(route);
        notificationMessage.setReadFlag(false);
        System.out.println(notificationMessage);
        notificationRepository.save(notificationMessage);
    }


    @Override
    public void createLikeNotification(User targetUser, User sourceUser, Post likedPost) {
        createNotification(
            targetUser,
            sourceUser,
            "Liked",
            String.format("User %s liked your post: %s", sourceUser.getUsername(), likedPost.getTitle()),
            "BlogScreen/?id=" + likedPost.getId()
        );
    }

    @Override
    public void createReplyNotification(User targetUser, User sourceUser, Post likedPost) {
        createNotification(
                targetUser,
                sourceUser,
                "Reply",
                String.format("User %s replied your post: %s", sourceUser.getUsername(), likedPost.getTitle()),
                "BlogScreen/?id=" + likedPost.getId()
        );
    }

    @Override
    public void createNewPostNotification(User targetUser, User sourceUser, Post likedPost) {
        createNotification(
                targetUser,
                sourceUser,
                "Following",
                String.format("Subscribed user %s created a new post: %s", sourceUser.getUsername(), likedPost.getTitle()),
                "BlogScreen/?id=" + likedPost.getId()
        );
    }
}
