package com.example.insurance.subscription.service;

import com.example.insurance.subscription.controller.dto.SubscriptionDto;
import com.example.insurance.subscription.model.Subscription;

import java.util.List;

public interface SubscriptionService {
    List<Subscription> getAllSubscriptions();
    Subscription getSubscription(Long subscriptionId);
    Subscription addSubscription(SubscriptionDto subscriptionDto);
    Subscription modifySubscription(SubscriptionDto subscriptionDto);
    Subscription removeSubscription(Long subscriptionId);

    Boolean isExist(SubscriptionDto subscriptionDto);
    Boolean isExist(Long subscriptionId);
}
