package com.example.onefit.repository;

import com.example.onefit.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findById(long id);
    Optional<Subscription> findAll(long id);
}