package com.example.insurance.subscription.service.impl;

import com.example.insurance.subscription.controller.dto.SubscriptionDto;
import com.example.insurance.subscription.model.Subscription;
import com.example.insurance.subscription.repository.SubscriptionRepository;
import com.example.insurance.subscription.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }
    
    @Override
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    @Override
    public Subscription getSubscription(Long subscriptionId) {
        return subscriptionRepository.findById(subscriptionId).orElse(null);
    }

    @Override
    public Subscription addSubscription(SubscriptionDto subscriptionDto) {
        Subscription reward = new Subscription(subscriptionDto);

        return subscriptionRepository.save(reward);
    }

    @Override
    public Subscription modifySubscription(SubscriptionDto subscriptionDto) {
        Subscription reward = subscriptionRepository.getById(subscriptionDto.getId());

        reward = new Subscription(subscriptionDto);

        return subscriptionRepository.save(reward);
    }

    @Override
    public Subscription removeSubscription(Long subscriptionId) {
        Subscription reward = subscriptionRepository.getById(subscriptionId);
        subscriptionRepository.deleteById(subscriptionId);
        return reward;
    }

    @Override
    public Boolean isExist(SubscriptionDto subscriptionDto) {
        return isExist(subscriptionDto.getId());
    }

    @Override
    public Boolean isExist(Long subscriptionId) {
        return subscriptionRepository.findById(subscriptionId).isPresent();
    }
}
