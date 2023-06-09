package com.thss.androidbackend.service.forum;

import com.thss.androidbackend.exception.CustomException;
import com.thss.androidbackend.model.document.Post;
import com.thss.androidbackend.model.document.Reply;
import com.thss.androidbackend.model.document.User;
import com.thss.androidbackend.model.dto.post.PostCreateDto;
import com.thss.androidbackend.model.vo.forum.PostCover;
import com.thss.androidbackend.model.vo.forum.PostDetail;
import com.thss.androidbackend.model.vo.user.UserMeta;
import com.thss.androidbackend.repository.PostRepository;
import com.thss.androidbackend.repository.ReplyRepository;
import com.thss.androidbackend.repository.UserRepository;
import com.thss.androidbackend.service.notification.NotificationService;
import com.thss.androidbackend.service.security.SecurityService;
import com.thss.androidbackend.websocket.ChatWebsocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.convert.LazyLoadingProxy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final SecurityService securityService;
    private final UserRepository userRepository;
    private final ChatWebsocketHandler chatWebsocketHandler;
    private final NotificationService notificationService;

    public void create(String title, String content, List<String> images, List<String> tag, String location, String videoUrl){
        Post newPost = new Post();
        User user = securityService.getCurrentUser();
        newPost.setCreator(user);
        newPost.setTitle(title);
        newPost.setContent(content);
        newPost.setImages(images);
        newPost.setTag(tag);
        newPost.setLocation(location);
        newPost.setVideoUrl(videoUrl);
        System.out.println(videoUrl);
        postRepository.save(newPost);
        user.getPostList().add(newPost);
        userRepository.save(user);

        user.getSubscriberList().forEach(it -> notificationService.createNewPostNotification(it, user, newPost));
    }

    public PostCover getPostCover(String postId) {
        Optional<Post> post = postRepository.findById(postId);
        if(post.isEmpty()) {
            return null;
        }
        return getPostCover(post.get());
    }

    public PostDetail getPostDetail(String postId) {
        Optional<Post> post = postRepository.findById(postId);
        if(post.isEmpty()) {
            throw new CustomException(HttpStatus.NOT_FOUND, "Post not found");
        }
        return getPostDetail(post.get());
    }

    public void like(String postId){
        Optional<Post> post = postRepository.findById(postId);
        if(post.isEmpty()) {
            throw new CustomException(HttpStatus.NOT_FOUND, "Post not found");
        }
        User user = securityService.getCurrentUser();
        Post realPost = post.get();
        List<User> likes = realPost.getLikes();
        if(likes.stream().anyMatch(u -> u.getId().equals(user.getId()))){
            throw new CustomException(HttpStatus.BAD_REQUEST, "Already liked");
        } else {
            likes.add(user);
        }
        postRepository.save(realPost);

        notificationService.createLikeNotification(realPost.getCreator(), user, realPost);
    }

    public void unLike(String postId){
        Optional<Post> post = postRepository.findById(postId);
        if(post.isEmpty()) {
            throw new CustomException(HttpStatus.NOT_FOUND, "Post not found");
        }
        User user = securityService.getCurrentUser();
        Post realPost = post.get();
        if(!realPost.getLikes().remove(user)){
            throw new CustomException(HttpStatus.BAD_REQUEST, "Not liked yet");
        }
        postRepository.save(realPost);
    }
    public void collect(String postId){
        Optional<Post> post = postRepository.findById(postId);
        if(post.isEmpty()) {
            throw new CustomException(HttpStatus.NOT_FOUND, "Post not found");
        }
        User user = securityService.getCurrentUser();
        Post realPost = post.get();
        if(realPost.getCollects().stream().anyMatch(u -> u.equals(user))){
            throw new CustomException(HttpStatus.BAD_REQUEST, "Already collected");
        } else {
            realPost.getCollects().add(user);
        }
        if (!user.getCollection().stream().anyMatch(p -> p.equals(realPost))) {
            user.getCollection().add(realPost);
        }
        postRepository.save(realPost);
        userRepository.save(user);
    }
    public void unCollect(String postId){
        Optional<Post> post = postRepository.findById(postId);
        if(post.isEmpty()) {
            throw new CustomException(HttpStatus.NOT_FOUND, "Post not found");
        }
        User user = securityService.getCurrentUser();
        Post realPost = post.get();
        if(!realPost.getCollects().remove(user)){
            throw new CustomException(HttpStatus.BAD_REQUEST, "not collected");
        }
        if (user.getCollection().stream().anyMatch(p -> p.equals(realPost))) {
            user.getCollection().remove(realPost);
        }
        postRepository.save(realPost);
        userRepository.save(user);
    }
    public PostCover getPostCover(Post post) {
        User creator = post.getCreator();
        PostCover cover;
        if(securityService.isAnonymous()){
            cover = new PostCover(
                    post.getId(),
                    new UserMeta(
                            creator.getId(),
                            creator.getUsername(),
                            creator.getAvatarUrl()
                    ),
                    post.getCreateTime(),
                    post.getTitle(),
                    post.getContent(),
                    post.getVideoUrl(),
                    post.getImages(),
                    post.getTag(),
                    post.getLikes().size(),
                    post.getCollects().size(),
                    post.getComments().stream().map(Reply::getReplyVo).toList(),
                    false,
                    false,
                    post.getLocation()
            );
        } else {
            boolean liked = post.getLikes().stream().anyMatch(
                    u -> u.getId().equals(securityService.getCurrentUser().getId()));
            boolean collected = post.getCollects().stream().anyMatch(
                    u -> u.getId().equals(securityService.getCurrentUser().getId()));
            cover = new PostCover(
                    post.getId(),
                    new UserMeta(
                            creator.getId(),
                            creator.getUsername(),
                            creator.getAvatarUrl()
                    ),
                    post.getCreateTime(),
                    post.getTitle(),
                    post.getContent(),
                    post.getVideoUrl(),
                    post.getImages(),
                    post.getTag(),
                    post.getLikes().size(),
                    post.getCollects().size(),
                    post.getComments().stream().map(Reply::getReplyVo).toList(),
                    liked,
                    collected,
                    post.getLocation()
            );
        }

        return cover;
    }

    public PostDetail getPostDetail(Post realPost) {
            User creator = realPost.getCreator();
            PostDetail detail;
            if(securityService.isAnonymous()){
                detail = new PostDetail(
                        new UserMeta(
                                creator.getId(),
                                creator.getUsername(),
                                creator.getAvatarUrl()
                        ),
                        realPost.getTitle(),
                        realPost.getContent(),
                        realPost.getImages(),
                        realPost.getTag().stream().toList(),
                        realPost.getLikes().size(),
                        realPost.getComments().size(),
                        realPost.getShares(),
                        false,
                        realPost.getComments()
                );
            } else {
                boolean liked = realPost.getLikes().stream().anyMatch(
                        u -> u.getId().equals(securityService.getCurrentUser().getId()));
                detail = new PostDetail(
                        new UserMeta(
                                creator.getId(),
                                creator.getUsername(),
                                creator.getAvatarUrl()
                        ),
                        realPost.getTitle(),
                        realPost.getContent(),
                        realPost.getImages(),
                        realPost.getTag().stream().toList(),
                        realPost.getLikes().size(),
                        realPost.getComments().size(),
                        realPost.getShares(),
                        liked,
                        realPost.getComments()
                );
            }

            return detail;
    }



    public void addReply(String postId, Reply reply){
        Optional<Post> post = postRepository.findById(postId);
        if(post.isEmpty()) {
            throw new CustomException(HttpStatus.NOT_FOUND, "Post not found");
        }
        Post realPost = post.get();
        realPost.getComments().add(reply);
        postRepository.save(realPost);
        chatWebsocketHandler.sendMessage();

        notificationService.createReplyNotification(realPost.getCreator(), reply.getCreator(), realPost);
    }

    @Override
    public void deleteReply(String postId, String replyId) {
        Optional<Post> post = postRepository.findById(postId);
        if(post.isEmpty()) {
            throw new CustomException(HttpStatus.NOT_FOUND, "Post not found");
        }
        Post realPost = post.get();
        realPost.getComments().removeIf(reply -> reply.getId().equals(replyId));
        postRepository.save(realPost);
    }

    @Override
    public Set<Post> search(String keyword) {
        List<Post> posts = postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrTagContainsIgnoreCase(keyword, keyword, keyword);
        Set<Post> postSet = new HashSet<>(posts);
        List<User> users = userRepository.findByUsernameContainingIgnoreCase(keyword);
        for(User user: users){
            postSet.addAll(user.getPostList());
        }
        return postSet;
    }
}
