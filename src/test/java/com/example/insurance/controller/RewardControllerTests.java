package com.example.insurance.controller;

import com.example.insurance.api.ApiResult;
import com.example.insurance.reward.controller.dto.RewardDto;
import com.example.insurance.reward.model.Reward;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RewardControllerTests {
    private final MockMvc mockMvc;

    @Autowired
    public RewardControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void test_get_all_rewards() throws Exception {
        ResultActions result =
                mockMvc.perform(
                        get("/api/v1/insurance/reward")
                                .accept(MediaType.APPLICATION_JSON)
                                );

        result.andDo(print()).andExpect(status().isOk());

        ApiResult<List<Reward>> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, TypeToken.getParameterized(List.class, Reward.class).getType()).getType());

        System.out.println(apiResult);
    }

    @Test
    void test_get_reward_by_id() throws  Exception {
        Reward reward = getAnyReward();

        Assertions.assertThat(reward)
                .isNotNull()
                .isEqualTo(getRewardById(reward.getId()));
    }

    @Test
    void test_get_reward_by_id_not_found() throws Exception {
        ResultActions result =
                mockMvc.perform(
                        get("/api/v1/insurance/reward/{rewardId}", 7777L)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        result.andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    void test_add_reward() throws Exception {
        createAndSaveAnyReward();
    }

    @Test
    void test_add_reward_with_already_exist_reward() throws Exception {
        Reward reward = getAnyReward();

        reward.setName("Added But Modified Reward");
        ResultActions result =
                mockMvc.perform(
                        post("/api/v1/insurance/reward")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(reward.toString())
                );

        result.andDo(print()).andExpect(status().isOk());

        ApiResult<Reward> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, Reward.class).getType());

        Reward addedButModifiedReward = apiResult.getData();

        Assertions.assertThat(addedButModifiedReward)
                .isNotNull()
                .isEqualTo(getRewardById(addedButModifiedReward.getId()));
    }

    @Test
    void test_modify_reward() throws Exception {
        Reward reward = getAnyReward();

        reward.setName("CHANGED Reward");

        ResultActions result =
                mockMvc.perform(
                        put("/api/v1/insurance/reward/{rewardId}", reward.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new RewardDto(reward).toString())
                );

        result.andDo(print()).andExpect(status().isOk());

        ApiResult<Reward> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, Reward.class).getType());

        Reward modifiedReward = apiResult.getData();

        Assertions.assertThat(modifiedReward)
                .isNotNull()
                .isEqualTo(reward)
                .isEqualTo(getRewardById(modifiedReward.getId()));

    }

    @Test
    void test_modify_reward_with_not_exists_reward() throws Exception {
        Reward reward = new Reward();
        reward.setName("NEW CONTRACT");
        reward.setId(7777L);

        ResultActions result =
                mockMvc.perform(
                        put("/api/v1/insurance/reward/{rewardId}", reward.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new RewardDto(reward).toString())
                );

        result.andDo(print()).andExpect(status().isOk());

        ApiResult<Reward> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, Reward.class).getType());

        Reward modifiedButAddedReward = apiResult.getData();

        Assertions.assertThat(modifiedButAddedReward)
                .isNotNull();

    }

    @Test
    void test_remove_reward() throws Exception {
        Reward reward = createAndSaveAnyReward();

        System.out.println(reward);
        ResultActions result =
                mockMvc.perform(
                        delete("/api/v1/insurance/reward/{rewardId}", reward.getId())
                                .accept(MediaType.APPLICATION_JSON)
                );

        result.andDo(print()).andExpect(status().isOk());

        ApiResult<Reward> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, Reward.class).getType());

        Reward deletedReward = apiResult.getData();

        result =
                mockMvc.perform(
                        get("/api/v1/insurance/reward/{rewardId}", deletedReward.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        result.andDo(print()).andExpect(status().isNotFound());
    }

    private Reward getAnyReward() throws Exception {
        ResultActions result =
                mockMvc.perform(
                        get("/api/v1/insurance/reward")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        result.andDo(print()).andExpect(status().isOk());

        ApiResult<List<Reward>> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, TypeToken.getParameterized(List.class, Reward.class).getType()).getType());

        return apiResult.getData().stream().findAny().orElseThrow(() -> new Exception("There is no data."));
    }

    private Reward getRewardById(Long rewardId) throws Exception {
        ResultActions result =
                mockMvc.perform(
                        get("/api/v1/insurance/reward/{rewardId}", rewardId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        result.andDo(print()).andExpect(status().isOk());

        ApiResult<Reward> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class,  Reward.class).getType());

        Assertions.assertThat(apiResult.getData())
                .isNotNull();

        return apiResult.getData();
    }

    private Reward createAndSaveAnyReward() throws Exception {
        ResultActions result =
                mockMvc.perform(
                        post("/api/v1/insurance/reward")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new RewardDto(1L, "Reward01").toString())
                );

        result.andDo(print()).andExpect(status().isOk());

        ApiResult<Reward> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, Reward.class).getType());

        Reward addedReward = apiResult.getData();

        Assertions.assertThat(addedReward)
                .isNotNull()
                .isEqualTo(getRewardById(addedReward.getId()));

        return addedReward;
    }
}
