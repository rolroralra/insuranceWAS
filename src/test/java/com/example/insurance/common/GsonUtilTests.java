package com.example.insurance.common;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

public class GsonUtilTests {

    @ParameterizedTest
    @ValueSource(strings = {"1234", "\"1234\"", "{\"a\":1}", "[1,2,3,4]", "{\"a\": [\"1\",\"2\"],\"b\":2}"})
    void test_gson_util(String inputJsonString) throws IOException {
        Assertions.assertThat(GsonUtil.isJsonValid(inputJsonString)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"{a:[1,2,3}", "{\"a\":\"1}", "{\"a\": \"1\",\"2\"],\"b\":2}"})
    void test_gson_util_exception(String invalidJsonString) throws IOException {
        Assertions.assertThat(GsonUtil.isJsonValid(invalidJsonString)).isFalse();
    }

}
