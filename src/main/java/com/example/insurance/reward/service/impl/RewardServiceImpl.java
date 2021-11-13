package com.example.insurance.reward.service.impl;

import com.example.insurance.reward.controller.dto.RewardDto;
import com.example.insurance.reward.model.Reward;
import com.example.insurance.reward.repository.RewardRepository;
import com.example.insurance.reward.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RewardServiceImpl implements RewardService {
    private final RewardRepository rewardRepository;

    @Autowired
    public RewardServiceImpl(RewardRepository rewardRepository) {
        this.rewardRepository = rewardRepository;
    }

    @Override
    public List<Reward> getAllRewards() {
        return rewardRepository.findAll();
    }

    @Override
    public Reward getReward(Long contractId) {
        return rewardRepository.findById(contractId).orElse(null);
    }

    @Override
    public Reward addReward(RewardDto rewardDto) {
        Reward reward = new Reward(rewardDto);

        return rewardRepository.save(reward);
    }

    @Override
    public Reward modifyReward(RewardDto rewardDto) {
        Reward reward = rewardRepository.getById(rewardDto.getId());

        reward = new Reward(rewardDto);

        return rewardRepository.save(reward);
    }

    @Override
    public Reward removeReward(Long contractId) {
        Reward reward = rewardRepository.getById(contractId);
        rewardRepository.deleteById(contractId);
        return reward;
    }

    @Override
    public Boolean isExist(RewardDto rewardDto) {
        return isExist(rewardDto.getId());
    }

    @Override
    public Boolean isExist(Long rewardId) {
        return rewardRepository.findById(rewardId).isPresent();
    }
}
