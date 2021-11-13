package com.example.insurance.repository;

import com.example.insurance.subscription.model.Subscription;
import com.example.insurance.subscription.repository.SubscriptionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SubscriptionRepositoryTests {
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @BeforeEach
    void setUp() {
        subscriptionRepository.save(new Subscription());
    }

    @Test
    void test_find_all() {
        Assertions.assertThat(subscriptionRepository.findAll()).isNotEmpty();
    }

    @Test
    void test_find_by_id() {
        subscriptionRepository.findAll().forEach(contract ->
                Assertions.assertThat(subscriptionRepository.findById(contract.getId())).isPresent()
        );
    }

    @Test
    void test_save() {
        int originalSize = subscriptionRepository.findAll().size();

        Subscription subscription = subscriptionRepository.save(new Subscription());

        Assertions.assertThat(subscriptionRepository.findById(subscription.getId()))
                .isPresent();

        Assertions.assertThat(originalSize + 1 == subscriptionRepository.findAll().size()).isTrue();
    }
}
