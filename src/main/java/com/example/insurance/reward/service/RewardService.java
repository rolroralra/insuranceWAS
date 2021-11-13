package com.example.insurance.reward.service;

import com.example.insurance.reward.controller.dto.RewardDto;
import com.example.insurance.reward.model.Reward;

import java.util.List;

public interface RewardService {
    List<Reward> getAllRewards();
    Reward getReward(Long rewardId);
    Reward addReward(RewardDto rewardDto);
    Reward modifyReward(RewardDto rewardDto);
    Reward removeReward(Long rewardId);

    Boolean isExist(RewardDto rewardDto);
    Boolean isExist(Long rewardId);
}
