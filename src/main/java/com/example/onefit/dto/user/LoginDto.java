package com.example.onefit.dto.user;


public record LoginDto(
    String username,
    String password,
    String email
) {
}
