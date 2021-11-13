package com.example.insurance.service;

import com.example.insurance.reward.controller.dto.RewardDto;
import com.example.insurance.reward.model.Reward;
import com.example.insurance.reward.service.RewardService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class RewardServiceTest {
    @Autowired
    private RewardService rewardService;

    @BeforeEach
    void setUp() {
        rewardService.addReward(new RewardDto());
    }

    @Test
    void test_add_reward() {
        Reward reward = rewardService.addReward(new RewardDto());

        Assertions.assertThat(reward).isNotNull();
    }

    @Test
    void test_get_all_rewards() {
        Assertions.assertThat(rewardService.getAllRewards()).isNotEmpty();
    }

    @Test
    void test_get_reward_by_id() {
        Optional<Reward> anyReward = rewardService.getAllRewards().stream().findFirst();

        Assertions.assertThat(anyReward).isPresent();
        Assertions.assertThat(rewardService.getReward(anyReward.get().getId())).isNotNull();
    }

    @Test
    void test_modify_reward() {
        Optional<Reward> anyReward = rewardService.getAllRewards().stream().findFirst();

        Assertions.assertThat(anyReward).isPresent();

        RewardDto rewardDto = new RewardDto(anyReward.get());
        rewardDto.setName("TEST");

        Reward modifiedReward = rewardService.modifyReward(rewardDto);

        Assertions.assertThat(new RewardDto(modifiedReward)).isEqualTo(rewardDto);

        rewardDto.setName("CHANGED");
        modifiedReward = rewardService.modifyReward(rewardDto);

        Assertions.assertThat(new RewardDto(modifiedReward)).isEqualTo(rewardDto);
        Assertions.assertThat(rewardService.getReward(rewardDto.getId())).isEqualTo(modifiedReward);
    }

    @Test
    void test_remove_reward() {
        Optional<Reward> anyReward = rewardService.getAllRewards().stream().findFirst();

        int originalSize = rewardService.getAllRewards().size();

        Assertions.assertThat(anyReward).isPresent();
        Reward removedReward = rewardService.removeReward(anyReward.get().getId());

        Assertions.assertThat(removedReward.getId()).isEqualTo(anyReward.get().getId());
        Assertions.assertThat(originalSize - 1 == rewardService.getAllRewards().size()).isTrue();
    }

    @Test
    void test_is_exist_by_id() {
        Optional<Reward> anyReward = rewardService.getAllRewards().stream().findFirst();

        if (!anyReward.isPresent()) {
            Assertions.fail("There is no row record.");
            return;
        }

        Assertions.assertThat(rewardService.isExist(anyReward.get().getId())).isNotNull();
    }

    @Test
    void test_is_exist() {
        Optional<Reward> anyReward = rewardService.getAllRewards().stream().findFirst();

        if (!anyReward.isPresent()) {
            Assertions.fail("There is no row record.");
            return;
        }

        Assertions.assertThat(rewardService.isExist(new RewardDto(anyReward.get()))).isNotNull();
    }
}
