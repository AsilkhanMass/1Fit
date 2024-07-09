package com.example.onefit.service.imp;

import com.example.onefit.dto.post.PostDto;
import com.example.onefit.entity.Post;
import com.example.onefit.entity.Subscription;
import com.example.onefit.entity.UserEntity;
import com.example.onefit.exceptions.PostNotFoundException;
import com.example.onefit.exceptions.SubscriptionNotFoundException;
import com.example.onefit.exceptions.UserNotFoundException;
import com.example.onefit.repository.PostRepository;
import com.example.onefit.repository.SubscriptionRepository;
import com.example.onefit.repository.UserRepository;
import com.example.onefit.service.PostService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImp implements PostService {
    private final PostRepository postRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public PostServiceImp(PostRepository postRepository, SubscriptionRepository subscriptionRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PostDto create(PostDto postDto) {
        Subscription subscription = subscriptionRepository.findById(postDto.subscriptionId()).orElseThrow(
                () -> new SubscriptionNotFoundException("Subscription not found with id: " + postDto.subscriptionId())
        );
        UserEntity user = userRepository.findById(postDto.userId()).orElseThrow(
                () -> new UserNotFoundException("User not found with id: " + postDto.userId())
        );
        Post post = Post.builder()
                .title(postDto.title())
                .body(postDto.body())
                .status(postDto.status())
                .subscriptionId(subscription)
                .userId(user)
                .build();
        postRepository.save(post);
        return postDto;
    }

    @Override
    public PostDto delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new PostNotFoundException("Already there is no post with id: " + id)
        );
        postRepository.deleteById(id);
        return toDto(post);

    }

    @Override
    public List<PostDto> showAll() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

    }

    @Override
    public PostDto getById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new PostNotFoundException("Post not found with id: " + id)
        );
        return toDto(post);
    }

    public PostDto toDto(Post post) {
        return new PostDto(post.getId(),
                post.getTitle(),
                post.getBody(),
                post.getUserId().getId(),
                post.getSubscriptionId().getId(),
                post.getStatus(),
                post.getCreatedAt(),
                post.getUpdatedAt());
    }
}
