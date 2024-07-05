package com.example.onefit.controller;

import com.example.onefit.dto.post.PostDto;
import com.example.onefit.service.imp.PostServiceImp;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostServiceImp postServiceImp;

    public PostController(PostServiceImp postServiceImp) {
        this.postServiceImp = postServiceImp;
    }
    @GetMapping("/{id}")
    @Cacheable(value = "posts", key = "#id")
    public ResponseEntity<PostDto> getPost(@PathVariable Long id){
        return new ResponseEntity<>(postServiceImp.getById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @CachePut(value = "posts", key = "#id")
    public ResponseEntity<?> cretePost(@PathVariable Long id, @RequestBody PostDto postDto){
        PostDto createdPost = postServiceImp.create(postDto);
        if(createdPost!=null){
            return ResponseEntity.ok("New post created!");
        }else{
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "posts", key="#id")
    public ResponseEntity<PostDto> deletePost(@PathVariable Long id){
        return new ResponseEntity<>(postServiceImp.delete(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    @Cacheable("posts")
    public ResponseEntity<List<PostDto>> getAll(){
        return new ResponseEntity<>(postServiceImp.showAll(), HttpStatus.OK);
    }


}
