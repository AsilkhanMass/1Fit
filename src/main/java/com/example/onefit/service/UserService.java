package com.example.onefit.service;

import com.example.onefit.dto.user.LoginDto;
import com.example.onefit.dto.user.RegisterDto;
import com.example.onefit.dto.user.UserDto;
import com.example.onefit.dto.web.JwtResponse;

public interface UserService {
    JwtResponse login(LoginDto dto);
    void register(RegisterDto dto);
    boolean delete(Long id);
    boolean update(UserDto dto);
}
