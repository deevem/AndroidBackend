package com.thss.androidbackend.service.post;

import com.thss.androidbackend.exception.CustomException;
import com.thss.androidbackend.model.document.Post;
import com.thss.androidbackend.model.document.User;
import com.thss.androidbackend.model.dto.post.PostCreateDto;
import com.thss.androidbackend.model.vo.forum.PostCover;
import com.thss.androidbackend.model.vo.forum.PostDetail;
import com.thss.androidbackend.model.vo.user.UserMeta;
import com.thss.androidbackend.repository.PostRepository;
import com.thss.androidbackend.repository.UserRepository;
import com.thss.androidbackend.service.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.convert.LazyLoadingProxy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final SecurityService securityService;
    private final UserRepository userRepository;

    public void create(PostCreateDto dto){
        Post newPost = new Post();
        User user = securityService.getCurrentUser();
        newPost.setCreator(user);
        newPost.setContent(dto.content());
        newPost.setTitle(dto.title());
        postRepository.save(newPost);
        user.getPostList().add(newPost);
        userRepository.save(user);
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
        LazyLoadingProxy lazyUser = (LazyLoadingProxy) post.get().getLikes();
        Set<User> likes = (Set<User>) lazyUser.getTarget();
        System.out.println(likes);
        if(likes.stream().anyMatch(u -> u.getId().equals(user.getId()))){
            throw new CustomException(HttpStatus.BAD_REQUEST, "Already liked");
        } else {
            likes.add(user);
        }
        postRepository.save(realPost);
    }
    public PostCover getPostCover(Post post) {
        User creator = post.getCreator();
        PostCover cover;
        if(securityService.isAnonymous()){
            cover = new PostCover(
                    new UserMeta(
                            creator.getId(),
                            creator.getUsername(),
                            creator.getAvatarUrl()
                    ),
                    post.getTitle(),
                    post.getContent(),
                    post.getImages(),
                    post.getTag().stream().toList(),
                    post.getLikes().size(),
                    post.getComments().size(),
                    post.getShares(),
                    false
            );
        } else {
            boolean liked = post.getLikes().stream().anyMatch(
                    u -> u.getId().equals(securityService.getCurrentUser().getId()));
            cover = new PostCover(
                    new UserMeta(
                            creator.getId(),
                            creator.getAvatarUrl(),
                            creator.getNickname()
                    ),
                    post.getTitle(),
                    post.getContent(),
                    post.getImages(),
                    post.getTag().stream().toList(),
                    post.getLikes().size(),
                    post.getComments().size(),
                    post.getShares(),
                    liked
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
                                creator.getAvatarUrl(),
                                creator.getNickname()
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
}
