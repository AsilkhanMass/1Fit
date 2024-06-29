package com.example.onefit.dto.user;

import com.example.onefit.entity.Role;

import java.time.LocalDateTime;
import java.util.Set;

public record UserDto(
        String name,
        String username,
        String phone_number,
        String bio,
        String gender,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updateAt
) {
}
