package com.example.onefit.service;

import com.example.onefit.entity.Subscription;

import java.util.List;
import java.util.Optional;

public interface SubscriptionService {
    Subscription createSubscription(Subscription subscription);

    Optional<Subscription> updateSubscription(Subscription subscription);

    void deleteSubscription(Long id);

    List<Subscription> getAllSubscriptions();

    Optional<Subscription> getSubscriptionById(Long id);
}
