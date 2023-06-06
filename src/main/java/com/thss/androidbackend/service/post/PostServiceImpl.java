package com.thss.androidbackend.service.post;

import com.thss.androidbackend.exception.CustomException;
import com.thss.androidbackend.model.document.Post;
import com.thss.androidbackend.model.document.User;
import com.thss.androidbackend.model.dto.post.PostCreateDto;
import com.thss.androidbackend.model.vo.post.PostCover;
import com.thss.androidbackend.model.vo.user.UserMeta;
import com.thss.androidbackend.repository.PostRepository;
import com.thss.androidbackend.repository.UserRepository;
import com.thss.androidbackend.service.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.convert.LazyLoadingProxy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
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
    }

    @Override
    public PostCover getPostCover(String postId) {
        Optional<Post> post = postRepository.findById(postId);
        if(post.isEmpty()) {
            return null;
        }
        User creator = post.get().getCreator();
        Post realPost = post.get();
        PostCover cover;
        if(securityService.isAnonymous()){
            cover = new PostCover(
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
                    false
            );
        } else {
            boolean liked = realPost.getLikes().stream().anyMatch(
                    u -> u.getId().equals(securityService.getCurrentUser().getId()));
            cover = new PostCover(
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
                    liked
            );
        }

        return cover;
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
}
