package com.example.onefit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String username;
    private String password;
    private String phoneNumber;
    private String bio;
    private String gender;
    private String email;
    @ManyToMany
    private List<Role> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

}
