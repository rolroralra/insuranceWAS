package com.example.insurance.model;

import com.example.insurance.contract.model.Contract;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ContractTests {
    @Test
    void test_hash_code() {
        Contract any = new Contract();
        any.setId(1L);
        Contract other = new Contract();
        other.setId(2L);

        Assertions.assertThat(any.hashCode()).isNotEqualTo(other.hashCode());
    }
}
