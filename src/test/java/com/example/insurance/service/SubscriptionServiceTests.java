package com.example.insurance.service;

import com.example.insurance.subscription.controller.dto.SubscriptionDto;
import com.example.insurance.subscription.model.Subscription;
import com.example.insurance.subscription.service.SubscriptionService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class SubscriptionServiceTests {
    @Autowired
    private SubscriptionService subscriptionService;

    @BeforeEach
    void setUp() {
        subscriptionService.addSubscription(new SubscriptionDto());
    }

    @Test
    void test_add_subscription() {
        Subscription subscription = subscriptionService.addSubscription(new SubscriptionDto());

        Assertions.assertThat(subscription).isNotNull();
    }

    @Test
    void test_get_all_subscriptions() {
        Assertions.assertThat(subscriptionService.getAllSubscriptions()).isNotEmpty();
    }

    @Test
    void test_get_subscription_by_id() {
        Optional<Subscription> anySubscription = subscriptionService.getAllSubscriptions().stream().findFirst();

        Assertions.assertThat(anySubscription).isPresent();
        Assertions.assertThat(subscriptionService.getSubscription(anySubscription.get().getId())).isNotNull();
    }

    @Test
    void test_modify_subscription() {
        Optional<Subscription> anySubscription = subscriptionService.getAllSubscriptions().stream().findFirst();

        Assertions.assertThat(anySubscription).isPresent();

        SubscriptionDto subscriptionDto = new SubscriptionDto(anySubscription.get());
        subscriptionDto.setName("TEST");

        Subscription modifiedSubscription = subscriptionService.modifySubscription(subscriptionDto);

        Assertions.assertThat(new SubscriptionDto(modifiedSubscription)).isEqualTo(subscriptionDto);

        subscriptionDto.setName("CHANGED");
        modifiedSubscription = subscriptionService.modifySubscription(subscriptionDto);

        Assertions.assertThat(new SubscriptionDto(modifiedSubscription)).isEqualTo(subscriptionDto);
        Assertions.assertThat(subscriptionService.getSubscription(subscriptionDto.getId())).isEqualTo(modifiedSubscription);
    }

    @Test
    void test_remove_subscription() {
        Optional<Subscription> anySubscription = subscriptionService.getAllSubscriptions().stream().findFirst();

        int originalSize = subscriptionService.getAllSubscriptions().size();

        Assertions.assertThat(anySubscription).isPresent();
        Subscription removedSubscription = subscriptionService.removeSubscription(anySubscription.get().getId());

        Assertions.assertThat(removedSubscription.getId()).isEqualTo(anySubscription.get().getId());
        Assertions.assertThat(originalSize - 1 == subscriptionService.getAllSubscriptions().size()).isTrue();
    }

    @Test
    void test_is_exist_by_id() {
        Optional<Subscription> anySubscription = subscriptionService.getAllSubscriptions().stream().findFirst();

        if (!anySubscription.isPresent()) {
            Assertions.fail("There is no row record.");
            return;
        }

        Assertions.assertThat(subscriptionService.isExist(anySubscription.get().getId())).isNotNull();
    }

    @Test
    void test_is_exist() {
        Optional<Subscription> anySubscription = subscriptionService.getAllSubscriptions().stream().findFirst();

        if (!anySubscription.isPresent()) {
            Assertions.fail("There is no row record.");
            return;
        }

        Assertions.assertThat(subscriptionService.isExist(new SubscriptionDto(anySubscription.get()))).isNotNull();
    }
}
