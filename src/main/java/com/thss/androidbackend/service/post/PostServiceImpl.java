package com.thss.androidbackend.service.post;

import com.thss.androidbackend.exception.CustomException;
import com.thss.androidbackend.model.document.Post;
import com.thss.androidbackend.model.document.User;
import com.thss.androidbackend.model.dto.post.PostCreateDto;
import com.thss.androidbackend.model.vo.post.PostCover;
import com.thss.androidbackend.model.vo.post.PostCoverList;
import com.thss.androidbackend.model.vo.user.UserMeta;
import com.thss.androidbackend.repository.PostRepository;
import com.thss.androidbackend.repository.UserRepository;
import com.thss.androidbackend.service.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.convert.LazyLoadingProxy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
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

    public void create(PostCreateDto dto){
        Post newPost = new Post();
        User user = securityService.getCurrentUser();
        newPost.setCreator(user);
        newPost.setContent(dto.content());
        newPost.setTitle(dto.title());
        postRepository.save(newPost);
    }

    public List<PostCover> getAllPost() {
        List<Post> allPost = postRepository.findAll();
        List<PostCover> allPostCover = allPost.stream().map(realPost -> {
            User creator = realPost.getCreator();
            boolean liked = realPost.getLikes().stream().anyMatch(
                    u -> u.getId().equals(securityService.getCurrentUser().getId()));
            boolean collected = realPost.getCollects().stream().anyMatch(
                    u -> u.getId().equals(securityService.getCurrentUser().getId()));
            return new PostCover(
                    new UserMeta(
                            creator.getId(),
                            creator.getUsername(),
                            creator.getAvatarUrl()
                    ),
                    realPost.getCreateTime(),
                    realPost.getTitle(),
                    realPost.getContent(),
                    realPost.getImages(),
                    realPost.getTag(),
                    realPost.getLikes().size(),
                    realPost.getCollects().size(),
                    realPost.getComments(),
                    liked,
                    collected,
                    realPost.getLocation()
            );
        }).collect(Collectors.toList());
        return allPostCover;
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
                    realPost.getCreateTime(),
                    realPost.getTitle(),
                    realPost.getContent(),
                    realPost.getImages(),
                    realPost.getTag(),
                    realPost.getLikes().size(),
                    realPost.getCollects().size(),
                    realPost.getComments(),
                    false,
                    false,
                    realPost.getLocation()
            );
        } else {
            boolean liked = realPost.getLikes().stream().anyMatch(
                    u -> u.getId().equals(securityService.getCurrentUser().getId()));
            boolean collected = realPost.getCollects().stream().anyMatch(
                    u -> u.getId().equals(securityService.getCurrentUser().getId()));
            cover = new PostCover(
                    new UserMeta(
                            creator.getId(),
                            creator.getUsername(),
                            creator.getAvatarUrl()
                    ),
                    realPost.getCreateTime(),
                    realPost.getTitle(),
                    realPost.getContent(),
                    realPost.getImages(),
                    realPost.getTag(),
                    realPost.getLikes().size(),
                    realPost.getCollects().size(),
                    realPost.getComments(),
                    liked,
                    collected,
                    realPost.getLocation()
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
