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
import com.thss.androidbackend.service.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.convert.LazyLoadingProxy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
        newPost.setLocation(dto.location());
        postRepository.save(newPost);
        user.getPostList().add(getPostCover(newPost));
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
                    post.getId(),
                    new UserMeta(
                            creator.getId(),
                            creator.getUsername(),
                            creator.getAvatarUrl()
                    ),
                    post.getCreateTime(),
                    post.getTitle(),
                    post.getContent(),
                    post.getImages(),
                    post.getTag(),
                    post.getLikes().size(),
                    post.getCollects().size(),
                    post.getComments(),
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
                    post.getImages(),
                    post.getTag(),
                    post.getLikes().size(),
                    post.getCollects().size(),
                    post.getComments(),
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

    public List<PostCover> getAllPost() {
        List<Post> allPost = postRepository.findAll();
        List<PostCover> allPostCover = allPost.stream().map(realPost -> {
            User creator = realPost.getCreator();
            boolean liked = realPost.getLikes().stream().anyMatch(
                    u -> u.getId().equals(securityService.getCurrentUser().getId()));
            boolean collected = realPost.getCollects().stream().anyMatch(
                    u -> u.getId().equals(securityService.getCurrentUser().getId()));
            return new PostCover(
                    realPost.getId(),
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
}
