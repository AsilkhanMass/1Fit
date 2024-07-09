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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class PostServiceImpTest {
    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePostSuccess() {
        Long subscriptionId = 1L;
        Long userId = 1L;
        String title = "Test Title";
        String body = "Test Body";
        String status = "Draft";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        PostDto postDto = new PostDto(null, title, body, userId, subscriptionId, status, createdAt, updatedAt);
        Subscription subscription = new Subscription();
        UserEntity user = new UserEntity();

        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(subscription));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        PostDto result = postService.create(postDto);

        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    public void testCreatePostSubscriptionNotFound() {
        Long subscriptionId = 1L;
        Long userId = 1L;
        String title = "Test Title";
        String body = "Test Body";
        String status = "Draft";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        PostDto postDto = new PostDto(null, title, body, userId, subscriptionId, status, createdAt, updatedAt);

        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.empty());

        assertThrows(SubscriptionNotFoundException.class, () -> postService.create(postDto));
    }

    @Test
    public void testCreatePostUserNotFound() {
        Long subscriptionId = 1L;
        Long userId = 1L;
        String title = "Test Title";
        String body = "Test Body";
        String status = "Draft";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        PostDto postDto = new PostDto(null, title, body, userId, subscriptionId, status, createdAt, updatedAt);
        Subscription subscription = new Subscription();

        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(subscription));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> postService.create(postDto));
    }

    @Test
    public void testDeletePostSuccess() {
        Long postId = 1L;
        Subscription subscription = new Subscription();
        UserEntity user = new UserEntity();
        Post post = Post.builder()
                .id(postId)
                .title("Test Title")
                .body("Test Body")
                .status("Draft")
                .subscriptionId(subscription)
                .userId(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        PostDto result = postService.delete(postId);

        verify(postRepository, times(1)).deleteById(postId);
        assertEquals(postId, result.id());
        assertEquals(post.getTitle(), result.title());
        assertEquals(post.getBody(), result.body());
        assertEquals(post.getUserId().getId(), result.userId());
        assertEquals(post.getSubscriptionId().getId(), result.subscriptionId());
        assertEquals(post.getStatus(), result.status());
        assertEquals(post.getCreatedAt(), result.createdAt());
        assertEquals(post.getUpdatedAt(), result.updatedAt());
    }

    @Test
    public void testDeletePostNotFound() {
        Long postId = 1L;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.delete(postId));
    }

    @Test
    public void testShowAllPosts() {
        Subscription subscription1 = new Subscription();
        Subscription subscription2 = new Subscription();
        UserEntity user1 = new UserEntity();
        UserEntity user2 = new UserEntity();
        Post post1 = Post.builder()
                .id(1L)
                .title("Test Title 1")
                .body("Test Body 1")
                .status("Draft")
                .subscriptionId(subscription1)
                .userId(user1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Post post2 = Post.builder()
                .id(2L)
                .title("Test Title 2")
                .body("Test Body 2")
                .status("Published")
                .subscriptionId(subscription2)
                .userId(user2)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(postRepository.findAll()).thenReturn(Arrays.asList(post1, post2));

        List<PostDto> result = postService.showAll();

        assertEquals(2, result.size());
        assertEquals(post1.getId(), result.get(0).id());
        assertEquals(post1.getTitle(), result.get(0).title());
        assertEquals(post1.getBody(), result.get(0).body());
        assertEquals(post1.getUserId().getId(), result.get(0).userId());
        assertEquals(post1.getSubscriptionId().getId(), result.get(0).subscriptionId());
        assertEquals(post1.getStatus(), result.get(0).status());
        assertEquals(post1.getCreatedAt(), result.get(0).createdAt());
        assertEquals(post1.getUpdatedAt(), result.get(0).updatedAt());

        assertEquals(post2.getId(), result.get(1).id());
        assertEquals(post2.getTitle(), result.get(1).title());
        assertEquals(post2.getBody(), result.get(1).body());
        assertEquals(post2.getUserId().getId(), result.get(1).userId());
        assertEquals(post2.getSubscriptionId().getId(), result.get(1).subscriptionId());
        assertEquals(post2.getStatus(), result.get(1).status());
        assertEquals(post2.getCreatedAt(), result.get(1).createdAt());
        assertEquals(post2.getUpdatedAt(), result.get(1).updatedAt());
    }

    @Test
    public void testGetByIdSuccess() {
        Long postId = 1L;
        Subscription subscription = new Subscription();
        UserEntity user = new UserEntity();
        Post post = Post.builder()
                .id(postId)
                .title("Test Title")
                .body("Test Body")
                .status("Draft")
                .subscriptionId(subscription)
                .userId(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        PostDto result = postService.getById(postId);

        assertEquals(postId, result.id());
        assertEquals(post.getTitle(), result.title());
        assertEquals(post.getBody(), result.body());
        assertEquals(post.getUserId().getId(), result.userId());
        assertEquals(post.getSubscriptionId().getId(), result.subscriptionId());
        assertEquals(post.getStatus(), result.status());
        assertEquals(post.getCreatedAt(), result.createdAt());
        assertEquals(post.getUpdatedAt(), result.updatedAt());
    }

    @Test
    public void testGetByIdNotFound() {
        Long postId = 1L;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.getById(postId));
    }

}