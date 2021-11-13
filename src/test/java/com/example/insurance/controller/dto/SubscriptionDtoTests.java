package com.example.insurance.controller.dto;

import com.example.insurance.common.GsonUtil;
import com.example.insurance.subscription.controller.dto.SubscriptionDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class SubscriptionDtoTests {
    @Test
    void test_to_string() {
        SubscriptionDto subscriptionDto = new SubscriptionDto();
        Assertions.assertThat(subscriptionDto.toString())
                .isNotEmpty()
                .isNotBlank();

        try {
            Assertions.assertThat(GsonUtil.isJsonValid(subscriptionDto.toString())).isTrue();
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail("Json Parser Failed.");
        }
    }

    @Test
    void test_all_args_constructor() {
        SubscriptionDto subscriptionDto = new SubscriptionDto(1L, "TEST");
        Assertions.assertThat(subscriptionDto).isNotNull();
    }
}
