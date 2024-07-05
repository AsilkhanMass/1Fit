package com.example.onefit.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String body;
    @ManyToOne
    private UserEntity userId;
    @ManyToOne
    private Subscription subscriptionId;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
