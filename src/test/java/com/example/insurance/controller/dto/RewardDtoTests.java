package com.example.insurance.controller.dto;

import com.example.insurance.common.GsonUtil;
import com.example.insurance.reward.controller.dto.RewardDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class RewardDtoTests {
    @Test
    void test_to_string() {
        RewardDto rewardDto = new RewardDto();
        Assertions.assertThat(rewardDto.toString())
                .isNotEmpty()
                .isNotBlank();

        try {
            Assertions.assertThat(GsonUtil.isJsonValid(rewardDto.toString())).isTrue();
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail("Json Parser Failed.");
        }
    }

    @Test
    void test_all_args_constructor() {
        RewardDto rewardDto = new RewardDto(1L, "TEST");
        Assertions.assertThat(rewardDto).isNotNull();
    }
}
