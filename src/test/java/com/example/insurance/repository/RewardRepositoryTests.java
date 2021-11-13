package com.example.insurance.repository;

import com.example.insurance.reward.model.Reward;
import com.example.insurance.reward.repository.RewardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RewardRepositoryTests {
    @Autowired
    private RewardRepository rewardRepository;

    @BeforeEach
    void setUp() {
        rewardRepository.save(new Reward());
    }

    @Test
    void test_find_all() {
        Assertions.assertThat(rewardRepository.findAll()).isNotEmpty();
    }

    @Test
    void test_find_by_id() {
        rewardRepository.findAll().forEach(contract ->
                Assertions.assertThat(rewardRepository.findById(contract.getId())).isPresent()
        );
    }

    @Test
    void test_save() {
        int originalSize = rewardRepository.findAll().size();

        Reward reward = rewardRepository.save(new Reward());

        Assertions.assertThat(rewardRepository.findById(reward.getId()))
                .isPresent();

        Assertions.assertThat(originalSize + 1 == rewardRepository.findAll().size()).isTrue();
    }
}
