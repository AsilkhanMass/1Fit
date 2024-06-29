package com.example.onefit.controller;

import com.example.onefit.dto.user.LoginDto;
import com.example.onefit.dto.user.RegisterDto;
import com.example.onefit.dto.user.UserDto;
import com.example.onefit.dto.web.JwtResponse;
import com.example.onefit.service.imp.UserServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserServiceImp userServiceImp;

    public UserController(UserServiceImp userServiceImp) {
        this.userServiceImp = userServiceImp;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginDto dto) {
        return ResponseEntity.ok(userServiceImp.login(dto));
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto){
        userServiceImp.register(registerDto);
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id){
        boolean checker = userServiceImp.delete(id);
        if(checker){
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);

    }
    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<String> update(@RequestBody UserDto dto){
        boolean checker = userServiceImp.update(dto);
        if(checker){
            return new ResponseEntity<>("User updated!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
    }

}
