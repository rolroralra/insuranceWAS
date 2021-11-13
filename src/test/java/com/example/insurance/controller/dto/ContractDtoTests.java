package com.example.insurance.controller.dto;

import com.example.insurance.common.GsonUtil;
import com.example.insurance.contract.controller.dto.ContractDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class ContractDtoTests {
    @Test
    void test_to_string() {
        ContractDto contractDto = new ContractDto();
        Assertions.assertThat(contractDto.toString())
                .isNotEmpty()
                .isNotBlank();

        try {
            Assertions.assertThat(GsonUtil.isJsonValid(contractDto.toString())).isTrue();
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail("Json Parser Failed.");
        }
    }

    @Test
    void test_all_args_constructor() {
        ContractDto contractDto = new ContractDto(1L, "TEST");
        Assertions.assertThat(contractDto).isNotNull();
    }
}
