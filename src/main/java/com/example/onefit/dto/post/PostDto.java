package com.example.onefit.dto.post;

import java.time.LocalDateTime;

public record PostDto(
        Long id,
        String title,
        String body,
        Long userId,
        Long subscriptionId,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}
