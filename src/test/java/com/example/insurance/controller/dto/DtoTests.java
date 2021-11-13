package com.example.insurance.controller.dto;

import com.example.insurance.common.GsonUtil;
import com.example.insurance.contract.controller.dto.ContractDto;
import com.example.insurance.reward.controller.dto.RewardDto;
import com.example.insurance.subscription.controller.dto.SubscriptionDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class DtoTests {

    @ParameterizedTest
    @ValueSource(classes = {SubscriptionDto.class, RewardDto.class, ContractDto.class})
    void test_to_string(Class<?> dtoClass) {
        try {
            Assertions.assertThat(dtoClass.newInstance().toString())
                    .isNotEmpty()
                    .isNotBlank();

            Assertions.assertThat(GsonUtil.isJsonValid(dtoClass.newInstance().toString())).isTrue();

        } catch (InstantiationException | IllegalAccessException | IOException e) {
            e.printStackTrace();
            Assertions.fail("Dto Class toString() method test Failed.");
        }
    }
}
