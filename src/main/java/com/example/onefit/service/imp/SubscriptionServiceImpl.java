package com.example.onefit.service.imp;

import com.example.onefit.entity.Subscription;
import com.example.onefit.repository.SubscriptionRepository;
import com.example.onefit.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public Subscription createSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Optional<Subscription> updateSubscription(Subscription subscription) {
        Optional<Subscription> existingSubscription = subscriptionRepository.findById(subscription.getId());
        if (existingSubscription.isPresent()) {

            Subscription sub = existingSubscription.get();
            if (subscription.getUserId() != null) {
                sub.setUserId(subscription.getUserId());
            }
            if (subscription.getSportId() != null) {
                sub.setSportId(subscription.getSportId());
            }
            if (subscription.getNumberOfVisits() != null) {
                sub.setNumberOfVisits(subscription.getNumberOfVisits());
            }
            return Optional.of(subscriptionRepository.save(sub));
        }
        return Optional.empty();
    }

    @Override
    public void deleteSubscription(Long id) {
        subscriptionRepository.deleteById(id);
    }

    @Override
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    @Override
    public Optional<Subscription> getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id);
    }
}
