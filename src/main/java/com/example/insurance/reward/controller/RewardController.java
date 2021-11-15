package com.example.insurance.reward.controller;

import com.example.insurance.api.ApiResult;
import com.example.insurance.reward.controller.dto.RewardDto;
import com.example.insurance.reward.model.Reward;
import com.example.insurance.reward.service.RewardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/insurance", produces = {MediaType.APPLICATION_JSON_VALUE})
public class RewardController {
    private final RewardService rewardService;

    @Autowired
    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @RequestMapping(value = "/reward", method = {RequestMethod.GET})
    ApiResult<List<Reward>> getAllRewards() {
        return ApiResult.succeed(rewardService.getAllRewards());
    }

    @RequestMapping(value = "/reward", method = {RequestMethod.POST})
    ApiResult<Reward> addReward(@RequestBody RewardDto rewardDto) {
        if (rewardService.isExist(rewardDto)) {
            return ApiResult.succeed(rewardService.modifyReward(rewardDto));
        }

        return ApiResult.succeed(rewardService.addReward(rewardDto));
    }

    @RequestMapping(value ="/reward/{rewardId}", method = {RequestMethod.PUT})
    ApiResult<Reward> modifyReward(@PathVariable(value = "rewardId") Long rewardId, @RequestBody RewardDto rewardDto) {
        rewardDto.setId(rewardId);

        if (!rewardService.isExist(rewardId)) {
            return ApiResult.succeed(rewardService.addReward(rewardDto));
        }

        return ApiResult.succeed(rewardService.modifyReward(rewardDto));
    }

    @RequestMapping(value = "/reward/{rewardId}", method = {RequestMethod.GET})
    ApiResult<Reward> getReward(@PathVariable(value = "rewardId") Long rewardId) {
        Reward searchedReward = rewardService.getReward(rewardId);
        if (Objects.isNull(searchedReward)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reward Not Found");
        }

        return ApiResult.succeed(searchedReward);
    }

    @RequestMapping(value ="/reward/{rewardId}", method = {RequestMethod.DELETE})
    ApiResult<Reward> removeReward(@PathVariable(value = "rewardId") Long rewardId) {
        return ApiResult.succeed(rewardService.removeReward(rewardId));
    }
}
