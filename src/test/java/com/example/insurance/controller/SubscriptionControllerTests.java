package com.example.insurance.controller;

import com.example.insurance.api.ApiResult;
import com.example.insurance.subscription.controller.dto.SubscriptionDto;
import com.example.insurance.subscription.model.Subscription;
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
public class SubscriptionControllerTests {
    private final MockMvc mockMvc;

    @Autowired
    public SubscriptionControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void test_get_all_subscriptions() throws Exception {
        ResultActions result =
                mockMvc.perform(
                        get("/api/v1/insurance/subscription")
                                .accept(MediaType.APPLICATION_JSON)
                                );

        result.andDo(print()).andExpect(status().isOk());

        ApiResult<List<Subscription>> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, TypeToken.getParameterized(List.class, Subscription.class).getType()).getType());

        System.out.println(apiResult);
    }

    @Test
    void test_get_subscription_by_id() throws  Exception {
        Subscription subscription = getAnySubscription();

        Assertions.assertThat(subscription)
                .isNotNull()
                .isEqualTo(getSubscriptionById(subscription.getId()));
    }

    @Test
    void test_get_subscription_by_id_not_found() throws Exception {
        ResultActions result =
                mockMvc.perform(
                        get("/api/v1/insurance/subscription/{subscriptionId}", 7777L)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        result.andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    void test_add_subscription() throws Exception {
        createAndSaveAnySubscription();
    }

    @Test
    void test_add_subscription_with_already_exist_subscription() throws Exception {
        Subscription subscription = getAnySubscription();

        subscription.setName("Added But Modified Subscription");
        ResultActions result =
                mockMvc.perform(
                        post("/api/v1/insurance/subscription")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(subscription.toString())
                );

        result.andDo(print()).andExpect(status().isOk());

        ApiResult<Subscription> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, Subscription.class).getType());

        Subscription addedButModifiedSubscription = apiResult.getData();

        Assertions.assertThat(addedButModifiedSubscription)
                .isNotNull()
                .isEqualTo(getSubscriptionById(addedButModifiedSubscription.getId()));
    }

    @Test
    void test_modify_subscription() throws Exception {
        Subscription subscription = getAnySubscription();

        subscription.setName("CHANGED Subscription");

        ResultActions result =
                mockMvc.perform(
                        put("/api/v1/insurance/subscription/{subscriptionId}", subscription.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new SubscriptionDto(subscription).toString())
                );

        result.andDo(print()).andExpect(status().isOk());

        ApiResult<Subscription> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, Subscription.class).getType());

        Subscription modifiedSubscription = apiResult.getData();

        Assertions.assertThat(modifiedSubscription)
                .isNotNull()
                .isEqualTo(subscription)
                .isEqualTo(getSubscriptionById(modifiedSubscription.getId()));

    }

    @Test
    void test_modify_subscription_with_not_exists_subscription() throws Exception {
        Subscription subscription = new Subscription();
        subscription.setName("NEW CONTRACT");
        subscription.setId(7777L);

        ResultActions result =
                mockMvc.perform(
                        put("/api/v1/insurance/subscription/{subscriptionId}", subscription.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new SubscriptionDto(subscription).toString())
                );

        result.andDo(print()).andExpect(status().isOk());

        ApiResult<Subscription> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, Subscription.class).getType());

        Subscription modifiedButAddedSubscription = apiResult.getData();

        Assertions.assertThat(modifiedButAddedSubscription)
                .isNotNull();

    }

    @Test
    void test_remove_subscription() throws Exception {
        Subscription subscription = createAndSaveAnySubscription();

        System.out.println(subscription);
        ResultActions result =
                mockMvc.perform(
                        delete("/api/v1/insurance/subscription/{subscriptionId}", subscription.getId())
                                .accept(MediaType.APPLICATION_JSON)
                );

        result.andDo(print()).andExpect(status().isOk());

        ApiResult<Subscription> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, Subscription.class).getType());

        Subscription deletedSubscription = apiResult.getData();

        result =
                mockMvc.perform(
                        get("/api/v1/insurance/subscription/{subscriptionId}", deletedSubscription.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        result.andDo(print()).andExpect(status().isNotFound());
    }

    private Subscription getAnySubscription() throws Exception {
        ResultActions result =
                mockMvc.perform(
                        get("/api/v1/insurance/subscription")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        result.andDo(print()).andExpect(status().isOk());

        ApiResult<List<Subscription>> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, TypeToken.getParameterized(List.class, Subscription.class).getType()).getType());

        return apiResult.getData().stream().findAny().orElseThrow(() -> new Exception("There is no data."));
    }

    private Subscription getSubscriptionById(Long subscriptionId) throws Exception {
        ResultActions result =
                mockMvc.perform(
                        get("/api/v1/insurance/subscription/{subscriptionId}", subscriptionId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        result.andDo(print()).andExpect(status().isOk());

        ApiResult<Subscription> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class,  Subscription.class).getType());

        Assertions.assertThat(apiResult.getData())
                .isNotNull();

        return apiResult.getData();
    }

    private Subscription createAndSaveAnySubscription() throws Exception {
        ResultActions result =
                mockMvc.perform(
                        post("/api/v1/insurance/subscription")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new SubscriptionDto(1L, "Subscription01").toString())
                );

        result.andDo(print()).andExpect(status().isOk());

        ApiResult<Subscription> apiResult = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), TypeToken.getParameterized(ApiResult.class, Subscription.class).getType());

        Subscription addedSubscription = apiResult.getData();

        Assertions.assertThat(addedSubscription)
                .isNotNull()
                .isEqualTo(getSubscriptionById(addedSubscription.getId()));

        return addedSubscription;
    }
}
