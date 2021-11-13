package com.example.insurance.model;

import com.example.insurance.reward.model.Reward;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RewardTests {
    @Test
    void test_hash_code() {
        Reward any = new Reward();
        any.setId(1L);
        Reward other = new Reward();
        other.setId(2L);

        Assertions.assertThat(any.hashCode()).isNotEqualTo(other.hashCode());
    }
}
