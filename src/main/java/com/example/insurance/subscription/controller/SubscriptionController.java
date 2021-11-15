package com.example.insurance.subscription.controller;

import com.example.insurance.api.ApiResult;
import com.example.insurance.subscription.controller.dto.SubscriptionDto;
import com.example.insurance.subscription.model.Subscription;
import com.example.insurance.subscription.service.SubscriptionService;
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
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @RequestMapping(value = "/subscription", method = {RequestMethod.GET})
    ApiResult<List<Subscription>> getAllSubscriptions() {
        return ApiResult.succeed(subscriptionService.getAllSubscriptions());
    }

    @RequestMapping(value = "/subscription", method = {RequestMethod.POST})
    ApiResult<Subscription> addSubscription(@RequestBody SubscriptionDto subscriptionDto) {
        if (subscriptionService.isExist(subscriptionDto)) {
            return ApiResult.succeed(subscriptionService.modifySubscription(subscriptionDto));
        }

        return ApiResult.succeed(subscriptionService.addSubscription(subscriptionDto));
    }

    @RequestMapping(value ="/subscription/{subscriptionId}", method = {RequestMethod.PUT})
    ApiResult<Subscription> modifySubscription(@PathVariable(value = "subscriptionId") Long subscriptionId, @RequestBody SubscriptionDto subscriptionDto) {
        subscriptionDto.setId(subscriptionId);

        if (!subscriptionService.isExist(subscriptionId)) {
            return ApiResult.succeed(subscriptionService.addSubscription(subscriptionDto));
        }

        return ApiResult.succeed(subscriptionService.modifySubscription(subscriptionDto));
    }

    @RequestMapping(value = "/subscription/{subscriptionId}", method = {RequestMethod.GET})
    ApiResult<Subscription> getSubscription(@PathVariable(value = "subscriptionId") Long subscriptionId) {
        Subscription searchedSubscription = subscriptionService.getSubscription(subscriptionId);
        if (Objects.isNull(searchedSubscription)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subscription Not Found");
        }

        return ApiResult.succeed(searchedSubscription);
    }

    @RequestMapping(value ="/subscription/{subscriptionId}", method = {RequestMethod.DELETE})
    ApiResult<Subscription> removeSubscription(@PathVariable(value = "subscriptionId") Long subscriptionId) {
        return ApiResult.succeed(subscriptionService.removeSubscription(subscriptionId));
    }
}
