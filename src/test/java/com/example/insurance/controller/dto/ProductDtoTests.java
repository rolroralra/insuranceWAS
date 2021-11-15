package com.example.insurance.controller.dto;

import com.example.insurance.common.GsonUtil;
import com.example.insurance.product.controller.dto.ProductDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class ProductDtoTests {
    @Test
    void test_to_string() {
        ProductDto productDto = new ProductDto();
        Assertions.assertThat(productDto.toString())
                .isNotEmpty()
                .isNotBlank();

        try {
            Assertions.assertThat(GsonUtil.isJsonValid(productDto.toString())).isTrue();
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail("Json Parser Failed.");
        }
    }

    @Test
    void test_all_args_constructor() {
        ProductDto productDto = new ProductDto(1L, "TEST");
        Assertions.assertThat(productDto).isNotNull();
    }
}
