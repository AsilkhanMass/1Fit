package com.example.onefit.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subscription")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private UserEntity userId;
    @OneToOne
    private SportTypeEntity sportId;
    private Short numberOfVisits;
    private Boolean isActive;
}
