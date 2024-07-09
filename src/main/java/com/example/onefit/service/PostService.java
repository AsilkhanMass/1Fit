package com.example.onefit.service;

import com.example.onefit.dto.post.PostDto;

import java.util.List;

public interface PostService {
    PostDto create(PostDto postDto);
    PostDto delete(Long id);
    List<PostDto> showAll();
    PostDto getById(Long id);
}
